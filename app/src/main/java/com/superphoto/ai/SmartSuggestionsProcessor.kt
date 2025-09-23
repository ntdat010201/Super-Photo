package com.superphoto.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.superphoto.config.APIConfig
import com.superphoto.constants.TransformationConstants
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

data class SmartSuggestion(
    val transformation: TransformationConstants.Transformation,
    val confidence: Float,
    val reason: String,
    val priority: Int
)

class SmartSuggestionsProcessor(private val context: Context) {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(APIConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(APIConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(APIConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()
    
    private val mainHandler = Handler(Looper.getMainLooper())

    companion object {
        private const val TAG = "SmartSuggestionsProcessor"
        private const val MAX_IMAGE_SIZE = APIConfig.MAX_IMAGE_SIZE
        private const val IMAGE_QUALITY = APIConfig.IMAGE_QUALITY
    }

    fun analyzeImageAndSuggest(
        imageUri: Uri,
        onSuccess: (List<SmartSuggestion>) -> Unit,
        onError: (String) -> Unit
    ) {
        Thread {
            try {
                val bitmap = loadAndResizeImage(imageUri)
                val base64Image = bitmapToBase64(bitmap)
                val analysisPrompt = createAnalysisPrompt()
                
                val suggestions = callGeminiForAnalysis(base64Image, analysisPrompt)
                
                // Post success callback to main thread
                mainHandler.post {
                    onSuccess(suggestions)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error analyzing image", e)
                // Post error callback to main thread
                mainHandler.post {
                    onError("Failed to analyze image: ${e.message}")
                }
            }
        }.start()
    }

    private fun loadAndResizeImage(uri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        return if (originalBitmap.width > MAX_IMAGE_SIZE || originalBitmap.height > MAX_IMAGE_SIZE) {
            val ratio = minOf(
                MAX_IMAGE_SIZE.toFloat() / originalBitmap.width,
                MAX_IMAGE_SIZE.toFloat() / originalBitmap.height
            )
            val newWidth = (originalBitmap.width * ratio).toInt()
            val newHeight = (originalBitmap.height * ratio).toInt()
            Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
        } else {
            originalBitmap
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream)
        val imageBytes = outputStream.toByteArray()
        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.NO_WRAP)
    }

    private fun createAnalysisPrompt(): String {
        return """
        Analyze this image and suggest the most suitable AI transformations based on image characteristics.
        
        Consider these factors:
        1. Image quality and lighting
        2. Subject matter (people, objects, landscapes, etc.)
        3. Color composition and saturation
        4. Background complexity
        5. Artistic potential
        6. Technical issues that could be improved
        
        Available transformations:
        - background_removal: Remove/replace background
        - face_swap: Swap faces between images
        - ai_enhance: Improve image quality and details
        - ai_colorize: Add color to black/white images
        - object_removal: Remove unwanted objects
        - style_transfer: Apply artistic styles
        - general_ai: General AI improvements
        
        Artistic styles available:
        - impressionist, expressionist, cubist, surrealist
        - pop_art, abstract, watercolor, oil_painting
        - sketch, anime, vintage, noir
        
        Return your analysis as JSON with this structure:
        {
          "suggestions": [
            {
              "transformation_id": "transformation_name",
              "confidence": 0.85,
              "reason": "Detailed explanation why this transformation is recommended",
              "priority": 1
            }
          ]
        }
        
        Provide 3-5 suggestions ranked by priority (1 = highest).
        Confidence should be 0.0-1.0 based on how well the transformation fits.
        """.trimIndent()
    }

    private fun callGeminiForAnalysis(base64Image: String, prompt: String): List<SmartSuggestion> {
        val requestBody = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                        put(JSONObject().apply {
                            put("inline_data", JSONObject().apply {
                                put("mime_type", "image/jpeg")
                                put("data", base64Image)
                            })
                        })
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("temperature", APIConfig.GENERATION_CONFIG_TEMPERATURE)
                put("topK", APIConfig.GENERATION_CONFIG_TOP_K)
                put("topP", APIConfig.GENERATION_CONFIG_TOP_P)
                put("maxOutputTokens", APIConfig.GENERATION_CONFIG_MAX_OUTPUT_TOKENS)
            })
            put("safetySettings", JSONArray().apply {
                APIConfig.SAFETY_SETTINGS.forEach { (category, threshold) ->
                    put(JSONObject().apply {
                        put("category", category)
                        put("threshold", threshold)
                    })
                }
            })
        }

        val request = Request.Builder()
            .url("${APIConfig.GEMINI_BASE_URL}${APIConfig.GEMINI_API_KEY}")
            .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected response: ${response.code}")
            }

            val responseBody = response.body?.string()
                ?: throw IOException("Empty response body")

            return parseGeminiResponse(responseBody)
        }
    }

    private fun parseGeminiResponse(responseBody: String): List<SmartSuggestion> {
        try {
            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.getJSONArray("candidates")
            
            if (candidates.length() == 0) {
                return emptyList()
            }

            val content = candidates.getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")

            // Extract JSON from the response text
            val jsonStart = content.indexOf("{")
            val jsonEnd = content.lastIndexOf("}") + 1
            
            if (jsonStart == -1 || jsonEnd <= jsonStart) {
                return getDefaultSuggestions()
            }

            val analysisJson = JSONObject(content.substring(jsonStart, jsonEnd))
            val suggestions = analysisJson.getJSONArray("suggestions")
            
            val result = mutableListOf<SmartSuggestion>()
            
            for (i in 0 until suggestions.length()) {
                val suggestion = suggestions.getJSONObject(i)
                val transformationId = suggestion.getString("transformation_id")
                val confidence = suggestion.getDouble("confidence").toFloat()
                val reason = suggestion.getString("reason")
                val priority = suggestion.getInt("priority")
                
                // Map transformation ID to actual transformation
                val transformation = mapTransformationId(transformationId)
                if (transformation != null) {
                    result.add(SmartSuggestion(transformation, confidence, reason, priority))
                }
            }
            
            return result.sortedBy { it.priority }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing Gemini response", e)
            return getDefaultSuggestions()
        }
    }

    private fun mapTransformationId(transformationId: String): TransformationConstants.Transformation? {
        return when (transformationId.lowercase()) {
            "background_removal" -> TransformationConstants.getTransformationById("background_remover")
            "face_swap" -> TransformationConstants.getTransformationById("face_swap")
            "ai_enhance" -> TransformationConstants.getTransformationById("ai_enhance")
            "ai_colorize" -> TransformationConstants.getTransformationById("enhance_colorize")
            "object_removal" -> TransformationConstants.getTransformationById("enhance_object_removal")
            "style_transfer", "impressionist" -> TransformationConstants.getTransformationById("style_transfer")
            "general_ai" -> TransformationConstants.getTransformationById("ai_enhance")
            else -> null
        }
    }

    private fun getDefaultSuggestions(): List<SmartSuggestion> {
        return listOf(
            SmartSuggestion(
                TransformationConstants.getTransformationById("ai_enhance")!!,
                0.8f,
                "AI enhancement can improve image quality and details",
                1
            ),
            SmartSuggestion(
                TransformationConstants.getTransformationById("background_remover")!!,
                0.7f,
                "Background removal can help focus on the main subject",
                2
            ),
            SmartSuggestion(
                TransformationConstants.getTransformationById("style_transfer")!!,
                0.6f,
                "Artistic style transfer can create interesting visual effects",
                3
            )
        )
    }

    fun getSuggestionsForImageType(imageType: ImageType): List<SmartSuggestion> {
        return when (imageType) {
            ImageType.PORTRAIT -> getPortraitSuggestions()
            ImageType.LANDSCAPE -> getLandscapeSuggestions()
            ImageType.OBJECT -> getObjectSuggestions()
            ImageType.BLACK_AND_WHITE -> getBWImageSuggestions()
            ImageType.LOW_QUALITY -> getLowQualitySuggestions()
            ImageType.UNKNOWN -> getDefaultSuggestions()
        }
    }

    private fun getPortraitSuggestions(): List<SmartSuggestion> {
        return listOf(
            SmartSuggestion(
                TransformationConstants.getTransformationById("ai_enhance")!!,
                0.9f,
                "Enhance facial features and skin details",
                1
            ),
            SmartSuggestion(
                TransformationConstants.getTransformationById("background_remover")!!,
                0.8f,
                "Remove background to focus on the person",
                2
            ),
            SmartSuggestion(
                TransformationConstants.getTransformationById("face_swap")!!,
                0.7f,
                "Swap faces for creative effects",
                3
            )
        )
    }

    private fun getLandscapeSuggestions(): List<SmartSuggestion> {
        return listOf(
            SmartSuggestion(
                TransformationConstants.getTransformationById("style_transfer")!!,
                0.9f,
                "Apply artistic styles to create stunning landscape art",
                1
            ),
            SmartSuggestion(
                TransformationConstants.getTransformationById("ai_enhance")!!,
                0.8f,
                "Enhance colors and details in the landscape",
                2
            ),
            SmartSuggestion(
                TransformationConstants.getTransformationById("enhance_object_removal")!!,
                0.6f,
                "Remove unwanted objects from the scene",
                3
            )
        )
    }

    private fun getObjectSuggestions(): List<SmartSuggestion> {
        return listOf(
            SmartSuggestion(
                TransformationConstants.getTransformationById("background_remover")!!,
                0.9f,
                "Remove background to isolate the object",
                1
            ),
            SmartSuggestion(
                TransformationConstants.getTransformationById("ai_enhance")!!,
                0.8f,
                "Enhance object details and clarity",
                2
            ),
            SmartSuggestion(
                TransformationConstants.getTransformationById("style_transfer")!!,
                0.7f,
                "Apply artistic effects to the object",
                3
            )
        )
    }

    private fun getBWImageSuggestions(): List<SmartSuggestion> {
        return listOf(
            SmartSuggestion(
                TransformationConstants.getTransformationById("enhance_colorize")!!,
                0.95f,
                "Add realistic colors to black and white image",
                1
            ),
            SmartSuggestion(
                TransformationConstants.getTransformationById("ai_enhance")!!,
                0.8f,
                "Enhance contrast and details",
                2
            ),
            SmartSuggestion(
                TransformationConstants.getTransformationById("style_transfer")!!,
                0.6f,
                "Apply artistic styles while maintaining B&W aesthetic",
                3
            )
        )
    }

    private fun getLowQualitySuggestions(): List<SmartSuggestion> {
        return listOf(
            SmartSuggestion(
                TransformationConstants.getTransformationById("ai_enhance")!!,
                0.95f,
                "Significantly improve image quality and resolution",
                1
            ),
            SmartSuggestion(
                TransformationConstants.getTransformationById("enhance_upscale")!!,
                0.8f,
                "Apply AI upscaling for better resolution",
                2
            ),
            SmartSuggestion(
                TransformationConstants.getTransformationById("style_transfer")!!,
                0.5f,
                "Artistic style might help mask quality issues",
                3
            )
        )
    }

    enum class ImageType {
        PORTRAIT,
        LANDSCAPE,
        OBJECT,
        BLACK_AND_WHITE,
        LOW_QUALITY,
        UNKNOWN
    }
}