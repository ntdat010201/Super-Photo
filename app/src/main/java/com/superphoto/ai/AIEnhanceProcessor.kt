package com.superphoto.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.superphoto.config.APIConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class AIEnhanceProcessor(private val context: Context) {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(APIConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(APIConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(APIConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()
    
    fun enhanceImage(
        imageUri: Uri,
        onSuccess: (Uri) -> Unit,
        onError: (String) -> Unit
    ) {
        Thread {
            try {
                // Load and prepare image
                val bitmap = loadImageFromUri(imageUri)
                if (bitmap == null) {
                    onError("Failed to load image")
                    return@Thread
                }
                
                // Create enhancement prompt
                val prompt = createEnhancementPrompt()
                
                // Call Gemini API for enhancement analysis
                val enhancementData = callGeminiAPI(bitmap, prompt)
                if (enhancementData == null) {
                    onError("Failed to analyze image for enhancement")
                    return@Thread
                }
                
                // Apply AI enhancement based on analysis
                val enhancedBitmap = applyAIEnhancement(bitmap, enhancementData)
                if (enhancedBitmap == null) {
                    onError("Failed to enhance image")
                    return@Thread
                }
                
                // Save enhanced image
                val enhancedUri = saveEnhancedImage(enhancedBitmap)
                if (enhancedUri != null) {
                    onSuccess(enhancedUri)
                } else {
                    onError("Failed to save enhanced image")
                }
                
            } catch (e: Exception) {
                Log.e("AIEnhanceProcessor", "Enhancement failed", e)
                onError("Enhancement failed: ${e.message}")
            }
        }.start()
    }
    
    private fun loadImageFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("AIEnhanceProcessor", "Failed to load image", e)
            null
        }
    }
    
    private fun createEnhancementPrompt(): String {
        return """
        Analyze this image and provide enhancement recommendations in JSON format:
        {
            "brightness": "adjustment_value (-100 to 100)",
            "contrast": "adjustment_value (-100 to 100)", 
            "saturation": "adjustment_value (-100 to 100)",
            "sharpness": "adjustment_value (0 to 200)",
            "noise_reduction": "strength (0 to 100)",
            "color_balance": {
                "red": "adjustment (-100 to 100)",
                "green": "adjustment (-100 to 100)", 
                "blue": "adjustment (-100 to 100)"
            },
            "highlights": "adjustment (-100 to 100)",
            "shadows": "adjustment (-100 to 100)",
            "clarity": "adjustment (0 to 100)"
        }
        
        Provide optimal values to enhance image quality, fix exposure issues, improve colors, and reduce noise.
        """.trimIndent()
    }
    
    private fun callGeminiAPI(bitmap: Bitmap, prompt: String): JSONObject? {
        return try {
            // Convert bitmap to base64
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, APIConfig.IMAGE_QUALITY, outputStream)
            val imageBytes = outputStream.toByteArray()
            val base64Image = android.util.Base64.encodeToString(imageBytes, android.util.Base64.NO_WRAP)
            
            // Create request body
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
            
            // Make API call
            val request = Request.Builder()
                .url("${APIConfig.GEMINI_BASE_URL}:generateContent?key=${APIConfig.GEMINI_API_KEY}")
                .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            
            if (response.isSuccessful && responseBody != null) {
                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.getJSONArray("candidates")
                if (candidates.length() > 0) {
                    val content = candidates.getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                    
                    // Parse JSON from response
                    val startIndex = content.indexOf("{")
                    val endIndex = content.lastIndexOf("}") + 1
                    if (startIndex != -1 && endIndex > startIndex) {
                        val jsonString = content.substring(startIndex, endIndex)
                        return JSONObject(jsonString)
                    }
                }
            }
            null
        } catch (e: Exception) {
            Log.e("AIEnhanceProcessor", "API call failed", e)
            null
        }
    }
    
    private fun applyAIEnhancement(bitmap: Bitmap, enhancementData: JSONObject): Bitmap? {
        return try {
            // Create a mutable copy of the bitmap
            val enhancedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            
            // Apply enhancement adjustments
            // Note: This is a simplified implementation
            // In a real app, you would use more sophisticated image processing libraries
            // like OpenCV or implement custom filters
            
            val brightness = enhancementData.optInt("brightness", 0)
            val contrast = enhancementData.optInt("contrast", 0)
            val saturation = enhancementData.optInt("saturation", 0)
            
            // Apply basic adjustments using ColorMatrix
            val colorMatrix = android.graphics.ColorMatrix()
            
            // Brightness adjustment
            if (brightness != 0) {
                val brightnessMatrix = android.graphics.ColorMatrix()
                val brightnessValue = brightness / 100f * 255f
                brightnessMatrix.set(floatArrayOf(
                    1f, 0f, 0f, 0f, brightnessValue,
                    0f, 1f, 0f, 0f, brightnessValue,
                    0f, 0f, 1f, 0f, brightnessValue,
                    0f, 0f, 0f, 1f, 0f
                ))
                colorMatrix.postConcat(brightnessMatrix)
            }
            
            // Contrast adjustment
            if (contrast != 0) {
                val contrastMatrix = android.graphics.ColorMatrix()
                val contrastValue = (contrast + 100) / 100f
                val offset = (1f - contrastValue) * 127.5f
                contrastMatrix.set(floatArrayOf(
                    contrastValue, 0f, 0f, 0f, offset,
                    0f, contrastValue, 0f, 0f, offset,
                    0f, 0f, contrastValue, 0f, offset,
                    0f, 0f, 0f, 1f, 0f
                ))
                colorMatrix.postConcat(contrastMatrix)
            }
            
            // Saturation adjustment
            if (saturation != 0) {
                val saturationMatrix = android.graphics.ColorMatrix()
                val saturationValue = (saturation + 100) / 100f
                saturationMatrix.setSaturation(saturationValue)
                colorMatrix.postConcat(saturationMatrix)
            }
            
            // Apply color matrix to bitmap
            val canvas = android.graphics.Canvas(enhancedBitmap)
            val paint = android.graphics.Paint()
            paint.colorFilter = android.graphics.ColorMatrixColorFilter(colorMatrix)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
            
            enhancedBitmap
        } catch (e: Exception) {
            Log.e("AIEnhanceProcessor", "Enhancement application failed", e)
            null
        }
    }
    
    private fun saveEnhancedImage(bitmap: Bitmap): Uri? {
        return try {
            val filename = "enhanced_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, filename)
            
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, APIConfig.IMAGE_QUALITY, outputStream)
            outputStream.close()
            
            Uri.fromFile(file)
        } catch (e: Exception) {
            Log.e("AIEnhanceProcessor", "Failed to save enhanced image", e)
            null
        }
    }
}