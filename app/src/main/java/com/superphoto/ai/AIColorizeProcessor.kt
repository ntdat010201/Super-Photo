package com.superphoto.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
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
import kotlin.math.max
import kotlin.math.min

class AIColorizeProcessor(private val context: Context) {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(APIConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(APIConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(APIConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()
    
    fun colorizeImage(
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
                
                // Check if image is already colored or needs colorization
                if (!isGrayscaleImage(bitmap)) {
                    onError("Image appears to already be in color. Please use a black and white image.")
                    return@Thread
                }
                
                // Create colorization prompt
                val prompt = createColorizationPrompt()
                
                // Call Gemini API for color analysis
                val colorData = callGeminiAPI(bitmap, prompt)
                if (colorData == null) {
                    onError("Failed to analyze image for colorization")
                    return@Thread
                }
                
                // Apply AI colorization based on analysis
                val colorizedBitmap = applyAIColorization(bitmap, colorData)
                if (colorizedBitmap == null) {
                    onError("Failed to colorize image")
                    return@Thread
                }
                
                // Save colorized image
                val colorizedUri = saveColorizedImage(colorizedBitmap)
                if (colorizedUri != null) {
                    onSuccess(colorizedUri)
                } else {
                    onError("Failed to save colorized image")
                }
                
            } catch (e: Exception) {
                Log.e("AIColorizeProcessor", "Colorization failed", e)
                onError("Colorization failed: ${e.message}")
            }
        }.start()
    }
    
    private fun loadImageFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("AIColorizeProcessor", "Failed to load image", e)
            null
        }
    }
    
    private fun isGrayscaleImage(bitmap: Bitmap): Boolean {
        // Sample pixels to check if image is grayscale
        val sampleSize = 100
        val width = bitmap.width
        val height = bitmap.height
        var colorPixelCount = 0
        var totalSamples = 0
        
        for (i in 0 until sampleSize) {
            val x = (Math.random() * width).toInt()
            val y = (Math.random() * height).toInt()
            
            val pixel = bitmap.getPixel(x, y)
            val red = Color.red(pixel)
            val green = Color.green(pixel)
            val blue = Color.blue(pixel)
            
            // Check if RGB values are significantly different (indicating color)
            val maxDiff = max(max(kotlin.math.abs(red - green), kotlin.math.abs(red - blue)), kotlin.math.abs(green - blue))
            if (maxDiff > 10) { // Threshold for color detection
                colorPixelCount++
            }
            totalSamples++
        }
        
        // If less than 5% of sampled pixels have color, consider it grayscale
        return (colorPixelCount.toFloat() / totalSamples) < 0.05f
    }
    
    private fun createColorizationPrompt(): String {
        return """
        Analyze this black and white image and provide realistic colorization suggestions in JSON format:
        {
            "scene_type": "description of the scene (portrait, landscape, urban, nature, etc.)",
            "time_period": "estimated time period (modern, vintage, historical, etc.)",
            "dominant_objects": [
                {
                    "object": "object name",
                    "suggested_color": "hex color code",
                    "confidence": "confidence level (0-100)"
                }
            ],
            "sky_color": "hex color for sky if visible",
            "skin_tone": "hex color for skin if person visible",
            "vegetation_color": "hex color for plants/trees if visible",
            "overall_tone": "warm/cool/neutral",
            "lighting_condition": "bright/dim/natural/artificial",
            "color_palette": [
                "hex_color_1",
                "hex_color_2", 
                "hex_color_3",
                "hex_color_4",
                "hex_color_5"
            ]
        }
        
        Provide realistic, historically accurate colors based on the content, time period, and context of the image.
        Use natural, believable colors that would be appropriate for the scene.
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
            Log.e("AIColorizeProcessor", "API call failed", e)
            null
        }
    }
    
    private fun applyAIColorization(bitmap: Bitmap, colorData: JSONObject): Bitmap? {
        return try {
            // Create a mutable copy of the bitmap
            val colorizedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            val width = bitmap.width
            val height = bitmap.height
            
            // Get color palette from AI analysis
            val colorPalette = mutableListOf<Int>()
            val paletteArray = colorData.optJSONArray("color_palette")
            if (paletteArray != null) {
                for (i in 0 until paletteArray.length()) {
                    try {
                        val hexColor = paletteArray.getString(i)
                        val color = Color.parseColor(hexColor)
                        colorPalette.add(color)
                    } catch (e: Exception) {
                        // Skip invalid colors
                    }
                }
            }
            
            // If no valid colors, use default palette
            if (colorPalette.isEmpty()) {
                colorPalette.addAll(listOf(
                    Color.parseColor("#8B4513"), // Brown
                    Color.parseColor("#228B22"), // Forest Green
                    Color.parseColor("#4169E1"), // Royal Blue
                    Color.parseColor("#DC143C"), // Crimson
                    Color.parseColor("#FFD700")  // Gold
                ))
            }
            
            // Get overall tone
            val overallTone = colorData.optString("overall_tone", "neutral")
            val toneMultiplier = when (overallTone) {
                "warm" -> floatArrayOf(1.1f, 1.0f, 0.9f)
                "cool" -> floatArrayOf(0.9f, 1.0f, 1.1f)
                else -> floatArrayOf(1.0f, 1.0f, 1.0f)
            }
            
            // Apply colorization using a simplified algorithm
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val originalPixel = bitmap.getPixel(x, y)
                    val gray = Color.red(originalPixel) // Since it's grayscale, R=G=B
                    val alpha = Color.alpha(originalPixel)
                    
                    // Map grayscale value to color palette
                    val colorIndex = (gray / 255.0f * (colorPalette.size - 1)).toInt()
                    val baseColor = colorPalette[min(colorIndex, colorPalette.size - 1)]
                    
                    // Extract RGB components
                    var red = Color.red(baseColor)
                    var green = Color.green(baseColor)
                    var blue = Color.blue(baseColor)
                    
                    // Apply brightness based on original grayscale value
                    val brightness = gray / 255.0f
                    red = (red * brightness * toneMultiplier[0]).toInt().coerceIn(0, 255)
                    green = (green * brightness * toneMultiplier[1]).toInt().coerceIn(0, 255)
                    blue = (blue * brightness * toneMultiplier[2]).toInt().coerceIn(0, 255)
                    
                    // Create new colored pixel
                    val coloredPixel = Color.argb(alpha, red, green, blue)
                    colorizedBitmap.setPixel(x, y, coloredPixel)
                }
            }
            
            // Apply subtle blending to make colors more natural
            applyColorBlending(colorizedBitmap)
            
            colorizedBitmap
        } catch (e: Exception) {
            Log.e("AIColorizeProcessor", "Colorization application failed", e)
            null
        }
    }
    
    private fun applyColorBlending(bitmap: Bitmap) {
        // Apply a subtle blur to blend colors more naturally
        // This is a simplified implementation - in production, you'd use more sophisticated algorithms
        val width = bitmap.width
        val height = bitmap.height
        val tempBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false)
        
        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                var totalRed = 0
                var totalGreen = 0
                var totalBlue = 0
                var totalAlpha = 0
                var count = 0
                
                // Sample surrounding pixels
                for (dy in -1..1) {
                    for (dx in -1..1) {
                        val pixel = tempBitmap.getPixel(x + dx, y + dy)
                        totalRed += Color.red(pixel)
                        totalGreen += Color.green(pixel)
                        totalBlue += Color.blue(pixel)
                        totalAlpha += Color.alpha(pixel)
                        count++
                    }
                }
                
                // Average the colors
                val avgRed = totalRed / count
                val avgGreen = totalGreen / count
                val avgBlue = totalBlue / count
                val avgAlpha = totalAlpha / count
                
                // Blend with original (70% original, 30% averaged)
                val originalPixel = tempBitmap.getPixel(x, y)
                val blendedRed = (Color.red(originalPixel) * 0.7f + avgRed * 0.3f).toInt()
                val blendedGreen = (Color.green(originalPixel) * 0.7f + avgGreen * 0.3f).toInt()
                val blendedBlue = (Color.blue(originalPixel) * 0.7f + avgBlue * 0.3f).toInt()
                
                val blendedPixel = Color.argb(avgAlpha, blendedRed, blendedGreen, blendedBlue)
                bitmap.setPixel(x, y, blendedPixel)
            }
        }
    }
    
    private fun saveColorizedImage(bitmap: Bitmap): Uri? {
        return try {
            val filename = "colorized_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, filename)
            
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, APIConfig.IMAGE_QUALITY, outputStream)
            outputStream.close()
            
            Uri.fromFile(file)
        } catch (e: Exception) {
            Log.e("AIColorizeProcessor", "Failed to save colorized image", e)
            null
        }
    }
}