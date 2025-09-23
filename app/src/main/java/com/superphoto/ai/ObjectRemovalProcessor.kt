package com.superphoto.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
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

class ObjectRemovalProcessor(private val context: Context) {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(APIConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(APIConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(APIConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()
    
    fun removeObject(
        imageUri: Uri,
        objectToRemove: String = "auto-detect", // User can specify object or let AI auto-detect
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
                
                // Create object detection prompt
                val prompt = createObjectDetectionPrompt(objectToRemove)
                
                // Call Gemini API for object detection and removal guidance
                val detectionData = callGeminiAPI(bitmap, prompt)
                if (detectionData == null) {
                    onError("Failed to detect objects for removal")
                    return@Thread
                }
                
                // Apply AI object removal based on detection
                val processedBitmap = applyObjectRemoval(bitmap, detectionData)
                if (processedBitmap == null) {
                    onError("Failed to remove object from image")
                    return@Thread
                }
                
                // Save processed image
                val processedUri = saveProcessedImage(processedBitmap)
                if (processedUri != null) {
                    onSuccess(processedUri)
                } else {
                    onError("Failed to save processed image")
                }
                
            } catch (e: Exception) {
                Log.e("ObjectRemovalProcessor", "Object removal failed", e)
                onError("Object removal failed: ${e.message}")
            }
        }.start()
    }
    
    private fun loadImageFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("ObjectRemovalProcessor", "Failed to load image", e)
            null
        }
    }
    
    private fun createObjectDetectionPrompt(objectToRemove: String): String {
        return if (objectToRemove == "auto-detect") {
            """
            Analyze this image and identify objects that could be removed to improve the composition. 
            Provide detection results in JSON format:
            {
                "detected_objects": [
                    {
                        "object": "object name",
                        "confidence": "confidence level (0-100)",
                        "bounding_box": {
                            "x": "x coordinate (0-1)",
                            "y": "y coordinate (0-1)", 
                            "width": "width (0-1)",
                            "height": "height (0-1)"
                        },
                        "removal_priority": "high/medium/low",
                        "removal_difficulty": "easy/medium/hard"
                    }
                ],
                "recommended_removal": "object name to remove",
                "removal_strategy": "inpainting/content_aware_fill/clone_stamp",
                "background_analysis": {
                    "type": "solid/gradient/textured/complex",
                    "dominant_color": "hex color",
                    "pattern": "description of background pattern"
                }
            }
            
            Focus on objects like: unwanted people, power lines, trash, watermarks, distracting elements.
            Prioritize objects that are easy to remove and would significantly improve the image.
            """.trimIndent()
        } else {
            """
            Analyze this image and locate the specified object: "$objectToRemove"
            Provide detection results in JSON format:
            {
                "target_object": {
                    "found": true/false,
                    "object": "$objectToRemove",
                    "confidence": "confidence level (0-100)",
                    "bounding_box": {
                        "x": "x coordinate (0-1)",
                        "y": "y coordinate (0-1)",
                        "width": "width (0-1)", 
                        "height": "height (0-1)"
                    },
                    "removal_difficulty": "easy/medium/hard"
                },
                "removal_strategy": "inpainting/content_aware_fill/clone_stamp",
                "background_analysis": {
                    "type": "solid/gradient/textured/complex",
                    "dominant_color": "hex color",
                    "pattern": "description of background pattern"
                },
                "surrounding_context": "description of area around the object"
            }
            
            If the object is not found, set "found" to false and suggest similar objects that were detected.
            """.trimIndent()
        }
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
            Log.e("ObjectRemovalProcessor", "API call failed", e)
            null
        }
    }
    
    private fun applyObjectRemoval(bitmap: Bitmap, detectionData: JSONObject): Bitmap? {
        return try {
            // Create a mutable copy of the bitmap
            val processedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(processedBitmap)
            
            // Get removal target
            val targetObject = getRemovalTarget(detectionData)
            if (targetObject == null) {
                Log.w("ObjectRemovalProcessor", "No valid removal target found")
                return bitmap // Return original if no object to remove
            }
            
            // Get bounding box
            val boundingBox = targetObject.optJSONObject("bounding_box")
            if (boundingBox == null) {
                Log.w("ObjectRemovalProcessor", "No bounding box found for target object")
                return bitmap
            }
            
            val x = (boundingBox.optDouble("x", 0.0) * bitmap.width).toFloat()
            val y = (boundingBox.optDouble("y", 0.0) * bitmap.height).toFloat()
            val width = (boundingBox.optDouble("width", 0.1) * bitmap.width).toFloat()
            val height = (boundingBox.optDouble("height", 0.1) * bitmap.height).toFloat()
            
            // Get background analysis
            val backgroundAnalysis = detectionData.optJSONObject("background_analysis")
            val backgroundType = backgroundAnalysis?.optString("type", "complex") ?: "complex"
            val dominantColor = backgroundAnalysis?.optString("dominant_color", "#FFFFFF") ?: "#FFFFFF"
            
            // Apply removal strategy based on background type
            when (backgroundType) {
                "solid" -> applySolidFill(canvas, x, y, width, height, dominantColor)
                "gradient" -> applyGradientFill(canvas, bitmap, x, y, width, height)
                "textured" -> applyTexturedFill(canvas, bitmap, x, y, width, height)
                else -> applyContentAwareFill(canvas, bitmap, x, y, width, height)
            }
            
            processedBitmap
        } catch (e: Exception) {
            Log.e("ObjectRemovalProcessor", "Object removal application failed", e)
            null
        }
    }
    
    private fun getRemovalTarget(detectionData: JSONObject): JSONObject? {
        // Try to get target object (for specific object removal)
        val targetObject = detectionData.optJSONObject("target_object")
        if (targetObject != null && targetObject.optBoolean("found", false)) {
            return targetObject
        }
        
        // Try to get recommended object (for auto-detection)
        val detectedObjects = detectionData.optJSONArray("detected_objects")
        val recommendedRemoval = detectionData.optString("recommended_removal", "")
        
        if (detectedObjects != null && recommendedRemoval.isNotEmpty()) {
            for (i in 0 until detectedObjects.length()) {
                val obj = detectedObjects.getJSONObject(i)
                if (obj.optString("object", "").contains(recommendedRemoval, ignoreCase = true)) {
                    return obj
                }
            }
            
            // If no exact match, return the first high-priority object
            for (i in 0 until detectedObjects.length()) {
                val obj = detectedObjects.getJSONObject(i)
                if (obj.optString("removal_priority", "low") == "high") {
                    return obj
                }
            }
        }
        
        return null
    }
    
    private fun applySolidFill(canvas: Canvas, x: Float, y: Float, width: Float, height: Float, colorHex: String) {
        try {
            val color = Color.parseColor(colorHex)
            val paint = Paint().apply {
                this.color = color
                isAntiAlias = true
            }
            canvas.drawRect(x, y, x + width, y + height, paint)
        } catch (e: Exception) {
            // Fallback to white if color parsing fails
            val paint = Paint().apply {
                color = Color.WHITE
                isAntiAlias = true
            }
            canvas.drawRect(x, y, x + width, y + height, paint)
        }
    }
    
    private fun applyGradientFill(canvas: Canvas, bitmap: Bitmap, x: Float, y: Float, width: Float, height: Float) {
        // Sample colors from surrounding area to create gradient
        val paint = Paint().apply {
            isAntiAlias = true
        }
        
        // Simple approach: blend with surrounding pixels
        val centerX = (x + width / 2).toInt().coerceIn(0, bitmap.width - 1)
        val centerY = (y + height / 2).toInt().coerceIn(0, bitmap.height - 1)
        
        // Sample surrounding colors
        val surroundingColors = mutableListOf<Int>()
        val sampleRadius = 20
        
        for (dy in -sampleRadius..sampleRadius step 5) {
            for (dx in -sampleRadius..sampleRadius step 5) {
                val sampleX = (centerX + dx).coerceIn(0, bitmap.width - 1)
                val sampleY = (centerY + dy).coerceIn(0, bitmap.height - 1)
                
                // Skip if inside the removal area
                if (sampleX >= x && sampleX <= x + width && sampleY >= y && sampleY <= y + height) {
                    continue
                }
                
                surroundingColors.add(bitmap.getPixel(sampleX, sampleY))
            }
        }
        
        // Use average color if we have samples
        if (surroundingColors.isNotEmpty()) {
            val avgColor = averageColors(surroundingColors)
            paint.color = avgColor
        } else {
            paint.color = Color.GRAY
        }
        
        canvas.drawRect(x, y, x + width, y + height, paint)
    }
    
    private fun applyTexturedFill(canvas: Canvas, bitmap: Bitmap, x: Float, y: Float, width: Float, height: Float) {
        // Clone nearby texture to fill the area
        val paint = Paint().apply {
            isAntiAlias = true
        }
        
        // Find a good source area to clone from
        val sourceX = if (x > width) (x - width).toInt() else (x + width * 1.5f).toInt()
        val sourceY = y.toInt()
        
        val sourceRect = android.graphics.Rect(
            sourceX.coerceIn(0, bitmap.width - width.toInt()),
            sourceY.coerceIn(0, bitmap.height - height.toInt()),
            (sourceX + width.toInt()).coerceIn(0, bitmap.width),
            (sourceY + height.toInt()).coerceIn(0, bitmap.height)
        )
        
        val destRect = android.graphics.Rect(
            x.toInt(),
            y.toInt(),
            (x + width).toInt(),
            (y + height).toInt()
        )
        
        canvas.drawBitmap(bitmap, sourceRect, destRect, paint)
    }
    
    private fun applyContentAwareFill(canvas: Canvas, bitmap: Bitmap, x: Float, y: Float, width: Float, height: Float) {
        // Simplified content-aware fill using surrounding pixel analysis
        val paint = Paint().apply {
            isAntiAlias = true
        }
        
        val fillWidth = width.toInt()
        val fillHeight = height.toInt()
        val startX = x.toInt()
        val startY = y.toInt()
        
        // Create a temporary bitmap for the fill area
        val fillBitmap = Bitmap.createBitmap(fillWidth, fillHeight, Bitmap.Config.ARGB_8888)
        
        for (py in 0 until fillHeight) {
            for (px in 0 until fillWidth) {
                val worldX = startX + px
                val worldY = startY + py
                
                // Find the best matching pixel from surrounding area
                val bestMatch = findBestMatchingPixel(bitmap, worldX, worldY, startX, startY, fillWidth, fillHeight)
                fillBitmap.setPixel(px, py, bestMatch)
            }
        }
        
        // Draw the filled area
        canvas.drawBitmap(fillBitmap, x, y, paint)
    }
    
    private fun findBestMatchingPixel(bitmap: Bitmap, targetX: Int, targetY: Int, 
                                    excludeX: Int, excludeY: Int, excludeWidth: Int, excludeHeight: Int): Int {
        // Simple approach: find similar pixel from surrounding area
        val searchRadius = 30
        val candidates = mutableListOf<Int>()
        
        for (dy in -searchRadius..searchRadius step 3) {
            for (dx in -searchRadius..searchRadius step 3) {
                val candidateX = (targetX + dx).coerceIn(0, bitmap.width - 1)
                val candidateY = (targetY + dy).coerceIn(0, bitmap.height - 1)
                
                // Skip if inside exclusion area
                if (candidateX >= excludeX && candidateX < excludeX + excludeWidth &&
                    candidateY >= excludeY && candidateY < excludeY + excludeHeight) {
                    continue
                }
                
                candidates.add(bitmap.getPixel(candidateX, candidateY))
            }
        }
        
        return if (candidates.isNotEmpty()) {
            candidates.random() // Simple random selection from candidates
        } else {
            Color.GRAY // Fallback color
        }
    }
    
    private fun averageColors(colors: List<Int>): Int {
        if (colors.isEmpty()) return Color.GRAY
        
        var totalRed = 0
        var totalGreen = 0
        var totalBlue = 0
        var totalAlpha = 0
        
        colors.forEach { color ->
            totalRed += Color.red(color)
            totalGreen += Color.green(color)
            totalBlue += Color.blue(color)
            totalAlpha += Color.alpha(color)
        }
        
        val count = colors.size
        return Color.argb(
            totalAlpha / count,
            totalRed / count,
            totalGreen / count,
            totalBlue / count
        )
    }
    
    private fun saveProcessedImage(bitmap: Bitmap): Uri? {
        return try {
            val filename = "object_removed_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, filename)
            
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, APIConfig.IMAGE_QUALITY, outputStream)
            outputStream.close()
            
            Uri.fromFile(file)
        } catch (e: Exception) {
            Log.e("ObjectRemovalProcessor", "Failed to save processed image", e)
            null
        }
    }
}