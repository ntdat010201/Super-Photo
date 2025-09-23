package com.superphoto.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.TimeUnit
import com.superphoto.config.APIConfig

class GeminiAIProcessor(private val context: Context) {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(APIConfig.API_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(APIConfig.API_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(APIConfig.API_TIMEOUT, TimeUnit.MILLISECONDS)
        .build()
        
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    fun processImage(
        imageUri: Uri,
        prompt: String,
        onSuccess: (Uri) -> Unit,
        onError: (String) -> Unit
    ) {
        scope.launch {
            try {
                // Convert image to base64
                val bitmap = loadBitmapFromUri(imageUri)
                val base64Image = bitmapToBase64(bitmap)
                
                // Create Gemini API request
                val response = callGeminiAPI(base64Image, prompt)
                
                // Process response and create result image
                val resultBitmap = processGeminiResponse(response, bitmap)
                
                // Save result and return URI
                val resultUri = saveBitmapToFile(resultBitmap)
                
                withContext(Dispatchers.Main) {
                    onSuccess(resultUri)
                }
                
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e.message ?: "Unknown error occurred")
                }
            }
        }
    }
    
    suspend fun loadBitmapFromUri(uri: Uri): Bitmap {
        return withContext(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            
            // Resize if too large (max size from config for API efficiency)
            val maxSize = APIConfig.MAX_IMAGE_SIZE
            if (bitmap.width > maxSize || bitmap.height > maxSize) {
                val ratio = minOf(maxSize.toFloat() / bitmap.width, maxSize.toFloat() / bitmap.height)
                val newWidth = (bitmap.width * ratio).toInt()
                val newHeight = (bitmap.height * ratio).toInt()
                Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
            } else {
                bitmap
            }
        }
    }
    
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, APIConfig.IMAGE_QUALITY, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.getEncoder().encodeToString(byteArray)
    }
    
    private suspend fun callGeminiAPI(base64Image: String, prompt: String): String {
        return withContext(Dispatchers.IO) {
            val requestBody = createGeminiRequestBody(base64Image, prompt)
            
            val request = Request.Builder()
                .url(APIConfig.getGeminiUrl())
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()
            
            val response = client.newCall(request).execute()
            
            if (!response.isSuccessful) {
                throw Exception("API call failed: ${response.code} ${response.message}")
            }
            
            response.body?.string() ?: throw Exception("Empty response body")
        }
    }
    
    private fun createGeminiRequestBody(base64Image: String, prompt: String): RequestBody {
        val json = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        // Add text prompt
                        put(JSONObject().apply {
                            put("text", buildPrompt(prompt))
                        })
                        // Add image
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
                put("temperature", APIConfig.DEFAULT_TEMPERATURE)
                put("topK", APIConfig.DEFAULT_TOP_K)
                put("topP", APIConfig.DEFAULT_TOP_P)
                put("maxOutputTokens", APIConfig.MAX_OUTPUT_TOKENS)
            })
            put("safetySettings", JSONArray().apply {
                // Add safety settings from config
                APIConfig.SAFETY_SETTINGS.forEach { (category, threshold) ->
                    put(JSONObject().apply {
                        put("category", category)
                        put("threshold", threshold)
                    })
                }
            })
        }
        
        return json.toString().toRequestBody("application/json".toMediaType())
    }
    
    private fun buildPrompt(basePrompt: String): String {
        return """
            $basePrompt
            
            Please analyze this image and provide detailed instructions for the requested transformation.
            Focus on:
            1. Key areas that need modification
            2. Color adjustments needed
            3. Specific techniques to apply
            4. Expected visual outcome
            
            Provide a comprehensive analysis that can guide image processing algorithms.
        """.trimIndent()
    }
    
    private suspend fun processGeminiResponse(response: String, originalBitmap: Bitmap): Bitmap {
        return withContext(Dispatchers.Default) {
            try {
                val jsonResponse = JSONObject(response)
                val candidates = jsonResponse.getJSONArray("candidates")
                
                if (candidates.length() > 0) {
                    val content = candidates.getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                    
                    // For now, apply basic image processing based on AI analysis
                    // In a real implementation, you would use the AI response to guide
                    // more sophisticated image processing algorithms
                    applyBasicTransformation(originalBitmap, content)
                } else {
                    throw Exception("No response from AI")
                }
            } catch (e: Exception) {
                // Fallback: apply basic enhancement
                applyBasicEnhancement(originalBitmap)
            }
        }
    }
    
    private fun applyBasicTransformation(bitmap: Bitmap, aiAnalysis: String): Bitmap {
        // This is a simplified implementation
        // In a real app, you would use the AI analysis to apply specific transformations
        
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        
        // Apply basic enhancements based on AI analysis keywords
        when {
            aiAnalysis.contains("brightness", ignoreCase = true) -> {
                adjustBrightness(mutableBitmap, 1.2f)
            }
            aiAnalysis.contains("contrast", ignoreCase = true) -> {
                adjustContrast(mutableBitmap, 1.3f)
            }
            aiAnalysis.contains("saturation", ignoreCase = true) -> {
                adjustSaturation(mutableBitmap, 1.4f)
            }
            else -> {
                // Default enhancement
                applyBasicEnhancement(mutableBitmap)
            }
        }
        
        return mutableBitmap
    }
    
    private fun applyBasicEnhancement(bitmap: Bitmap): Bitmap {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        
        // Apply basic AI-style enhancement
        adjustBrightness(mutableBitmap, 1.1f)
        adjustContrast(mutableBitmap, 1.2f)
        adjustSaturation(mutableBitmap, 1.3f)
        
        return mutableBitmap
    }
    
    private fun adjustBrightness(bitmap: Bitmap, factor: Float) {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        
        for (i in pixels.indices) {
            val pixel = pixels[i]
            val r = ((pixel shr 16 and 0xFF) * factor).coerceIn(0f, 255f).toInt()
            val g = ((pixel shr 8 and 0xFF) * factor).coerceIn(0f, 255f).toInt()
            val b = ((pixel and 0xFF) * factor).coerceIn(0f, 255f).toInt()
            val a = pixel shr 24 and 0xFF
            
            pixels[i] = (a shl 24) or (r shl 16) or (g shl 8) or b
        }
        
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    }
    
    private fun adjustContrast(bitmap: Bitmap, factor: Float) {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        
        for (i in pixels.indices) {
            val pixel = pixels[i]
            val r = (((pixel shr 16 and 0xFF) - 128) * factor + 128).coerceIn(0f, 255f).toInt()
            val g = (((pixel shr 8 and 0xFF) - 128) * factor + 128).coerceIn(0f, 255f).toInt()
            val b = (((pixel and 0xFF) - 128) * factor + 128).coerceIn(0f, 255f).toInt()
            val a = pixel shr 24 and 0xFF
            
            pixels[i] = (a shl 24) or (r shl 16) or (g shl 8) or b
        }
        
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    }
    
    private fun adjustSaturation(bitmap: Bitmap, factor: Float) {
        // Simplified saturation adjustment
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        
        for (i in pixels.indices) {
            val pixel = pixels[i]
            val r = pixel shr 16 and 0xFF
            val g = pixel shr 8 and 0xFF
            val b = pixel and 0xFF
            val a = pixel shr 24 and 0xFF
            
            // Convert to HSV for saturation adjustment
            val hsv = FloatArray(3)
            android.graphics.Color.RGBToHSV(r, g, b, hsv)
            hsv[1] = (hsv[1] * factor).coerceIn(0f, 1f)
            
            val newColor = android.graphics.Color.HSVToColor(hsv)
            pixels[i] = (a shl 24) or (newColor and 0xFFFFFF)
        }
        
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    }
    
    private suspend fun saveBitmapToFile(bitmap: Bitmap): Uri {
        return withContext(Dispatchers.IO) {
            val filename = "ai_transformed_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, filename)
            
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.close()
            
            Uri.fromFile(file)
        }
    }
    
    fun cleanup() {
        scope.cancel()
    }
}