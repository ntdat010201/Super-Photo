package com.superphoto.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
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
import kotlin.math.*

class StyleTransferProcessor(private val context: Context) {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(APIConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(APIConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(APIConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()
    
    // Predefined artistic styles
    enum class ArtisticStyle(val styleName: String, val description: String) {
        IMPRESSIONIST("impressionist", "Soft brushstrokes, light colors, outdoor scenes like Monet"),
        EXPRESSIONIST("expressionist", "Bold colors, emotional intensity like Van Gogh"),
        CUBIST("cubist", "Geometric shapes, multiple perspectives like Picasso"),
        SURREALIST("surrealist", "Dreamlike, fantastical elements like Dalí"),
        POP_ART("pop_art", "Bright colors, commercial imagery like Warhol"),
        ABSTRACT("abstract", "Non-representational, pure color and form"),
        WATERCOLOR("watercolor", "Transparent, flowing paint effects"),
        OIL_PAINTING("oil_painting", "Rich textures, classical painting techniques"),
        SKETCH("sketch", "Pencil or charcoal drawing style"),
        ANIME("anime", "Japanese animation art style"),
        VINTAGE("vintage", "Retro, aged photo effects"),
        NOIR("noir", "High contrast black and white, dramatic shadows")
    }
    
    fun transferStyle(
        imageUri: Uri,
        style: ArtisticStyle = ArtisticStyle.IMPRESSIONIST,
        intensity: Float = 0.8f, // 0.0 to 1.0
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
                
                // Create style transfer prompt
                val prompt = createStyleTransferPrompt(style, intensity)
                
                // Call Gemini API for style analysis and guidance
                val styleData = callGeminiAPI(bitmap, prompt)
                if (styleData == null) {
                    onError("Failed to analyze image for style transfer")
                    return@Thread
                }
                
                // Apply artistic style transformation
                val processedBitmap = applyStyleTransfer(bitmap, style, styleData, intensity)
                if (processedBitmap == null) {
                    onError("Failed to apply style transfer")
                    return@Thread
                }
                
                // Save processed image
                val processedUri = saveProcessedImage(processedBitmap, style.styleName)
                if (processedUri != null) {
                    onSuccess(processedUri)
                } else {
                    onError("Failed to save processed image")
                }
                
            } catch (e: Exception) {
                Log.e("StyleTransferProcessor", "Style transfer failed", e)
                onError("Style transfer failed: ${e.message}")
            }
        }.start()
    }
    
    fun transferStyleByName(
        imageUri: Uri,
        styleName: String,
        intensity: Float = 0.8f,
        onSuccess: (Uri) -> Unit,
        onError: (String) -> Unit
    ) {
        val style = ArtisticStyle.values().find { 
            it.styleName.equals(styleName, ignoreCase = true) 
        } ?: ArtisticStyle.IMPRESSIONIST
        
        transferStyle(imageUri, style, intensity, onSuccess, onError)
    }
    
    private fun loadImageFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("StyleTransferProcessor", "Failed to load image", e)
            null
        }
    }
    
    private fun createStyleTransferPrompt(style: ArtisticStyle, intensity: Float): String {
        return """
        Analyze this image for artistic style transfer to ${style.styleName} style.
        Style description: ${style.description}
        Intensity level: ${(intensity * 100).toInt()}%
        
        Provide analysis in JSON format:
        {
            "image_analysis": {
                "dominant_colors": ["hex color 1", "hex color 2", "hex color 3"],
                "composition": "description of main elements and layout",
                "lighting": "description of light sources and shadows",
                "texture": "description of surface textures",
                "mood": "emotional tone of the image"
            },
            "style_guidance": {
                "color_palette": {
                    "primary_colors": ["hex color 1", "hex color 2"],
                    "accent_colors": ["hex color 1", "hex color 2"],
                    "color_temperature": "warm/cool/neutral",
                    "saturation_adjustment": "increase/decrease/maintain",
                    "brightness_adjustment": "increase/decrease/maintain"
                },
                "brush_effects": {
                    "stroke_type": "smooth/rough/textured/geometric",
                    "stroke_direction": "horizontal/vertical/diagonal/circular/random",
                    "stroke_size": "fine/medium/bold",
                    "edge_treatment": "soft/sharp/blended"
                },
                "artistic_elements": {
                    "emphasis_areas": ["area 1", "area 2"],
                    "style_intensity": ${intensity},
                    "texture_overlay": "light/medium/heavy",
                    "contrast_adjustment": "increase/decrease/maintain"
                }
            },
            "transformation_steps": [
                "step 1 description",
                "step 2 description",
                "step 3 description"
            ]
        }
        
        Focus on how to transform this specific image to achieve the ${style.styleName} artistic style.
        Consider the image content, colors, and composition when providing guidance.
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
            Log.e("StyleTransferProcessor", "API call failed", e)
            null
        }
    }
    
    private fun applyStyleTransfer(bitmap: Bitmap, style: ArtisticStyle, styleData: JSONObject, intensity: Float): Bitmap? {
        return try {
            // Create a mutable copy of the bitmap
            val processedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            
            // Apply style-specific transformations
            when (style) {
                ArtisticStyle.IMPRESSIONIST -> applyImpressionistStyle(processedBitmap, styleData, intensity)
                ArtisticStyle.EXPRESSIONIST -> applyExpressionistStyle(processedBitmap, styleData, intensity)
                ArtisticStyle.CUBIST -> applyCubistStyle(processedBitmap, styleData, intensity)
                ArtisticStyle.SURREALIST -> applySurrealistStyle(processedBitmap, styleData, intensity)
                ArtisticStyle.POP_ART -> applyPopArtStyle(processedBitmap, styleData, intensity)
                ArtisticStyle.ABSTRACT -> applyAbstractStyle(processedBitmap, styleData, intensity)
                ArtisticStyle.WATERCOLOR -> applyWatercolorStyle(processedBitmap, styleData, intensity)
                ArtisticStyle.OIL_PAINTING -> applyOilPaintingStyle(processedBitmap, styleData, intensity)
                ArtisticStyle.SKETCH -> applySketchStyle(processedBitmap, styleData, intensity)
                ArtisticStyle.ANIME -> applyAnimeStyle(processedBitmap, styleData, intensity)
                ArtisticStyle.VINTAGE -> applyVintageStyle(processedBitmap, styleData, intensity)
                ArtisticStyle.NOIR -> applyNoirStyle(processedBitmap, styleData, intensity)
            }
            
            processedBitmap
        } catch (e: Exception) {
            Log.e("StyleTransferProcessor", "Style application failed", e)
            null
        }
    }
    
    private fun applyImpressionistStyle(bitmap: Bitmap, styleData: JSONObject, intensity: Float): Bitmap {
        // Soft brushstrokes and light colors
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            isAntiAlias = true
            alpha = (intensity * 255).toInt()
        }
        
        // Apply soft blur effect
        applySoftBlur(bitmap, intensity * 3)
        
        // Enhance warm colors
        applyColorAdjustment(bitmap, 1.2f, 1.1f, 0.9f, intensity)
        
        // Add texture overlay
        applyBrushTexture(canvas, paint, intensity)
        
        return bitmap
    }
    
    private fun applyExpressionistStyle(bitmap: Bitmap, styleData: JSONObject, intensity: Float): Bitmap {
        // Bold colors and emotional intensity
        val canvas = Canvas(bitmap)
        
        // Increase saturation and contrast
        applyColorAdjustment(bitmap, 1.5f, 1.3f, 1.2f, intensity)
        
        // Apply bold brush strokes
        applyBoldBrushStrokes(canvas, intensity)
        
        // Enhance edges
        applyEdgeEnhancement(bitmap, intensity)
        
        return bitmap
    }
    
    private fun applyCubistStyle(bitmap: Bitmap, styleData: JSONObject, intensity: Float): Bitmap {
        // Geometric shapes and multiple perspectives
        val canvas = Canvas(bitmap)
        
        // Apply geometric fragmentation effect
        applyGeometricFragmentation(canvas, bitmap, intensity)
        
        // Reduce color palette
        applyColorQuantization(bitmap, 8, intensity)
        
        return bitmap
    }
    
    private fun applySurrealistStyle(bitmap: Bitmap, styleData: JSONObject, intensity: Float): Bitmap {
        // Dreamlike and fantastical elements
        val canvas = Canvas(bitmap)
        
        // Apply dream-like blur and glow
        applySoftBlur(bitmap, intensity * 2)
        applyGlowEffect(canvas, intensity)
        
        // Enhance unusual colors
        applyColorAdjustment(bitmap, 1.3f, 1.4f, 1.1f, intensity)
        
        return bitmap
    }
    
    private fun applyPopArtStyle(bitmap: Bitmap, styleData: JSONObject, intensity: Float): Bitmap {
        // Bright colors and commercial imagery
        val canvas = Canvas(bitmap)
        
        // High contrast and saturation
        applyColorAdjustment(bitmap, 2.0f, 1.8f, 1.5f, intensity)
        
        // Apply posterization effect
        applyColorQuantization(bitmap, 6, intensity)
        
        // Add halftone pattern
        applyHalftonePattern(canvas, intensity)
        
        return bitmap
    }
    
    private fun applyAbstractStyle(bitmap: Bitmap, styleData: JSONObject, intensity: Float): Bitmap {
        // Non-representational, pure color and form
        val canvas = Canvas(bitmap)
        
        // Apply color field effects
        applyColorFieldEffect(canvas, bitmap, intensity)
        
        // Simplify forms
        applyFormSimplification(bitmap, intensity)
        
        return bitmap
    }
    
    private fun applyWatercolorStyle(bitmap: Bitmap, styleData: JSONObject, intensity: Float): Bitmap {
        // Transparent, flowing paint effects
        val canvas = Canvas(bitmap)
        
        // Apply watercolor bleeding effect
        applyWatercolorBleeding(canvas, bitmap, intensity)
        
        // Soften edges
        applySoftBlur(bitmap, intensity * 1.5f)
        
        // Adjust transparency
        applyTransparencyEffect(bitmap, intensity)
        
        return bitmap
    }
    
    private fun applyOilPaintingStyle(bitmap: Bitmap, styleData: JSONObject, intensity: Float): Bitmap {
        // Rich textures, classical painting techniques
        val canvas = Canvas(bitmap)
        
        // Apply oil paint texture
        applyOilPaintTexture(canvas, bitmap, intensity)
        
        // Enhance warm colors
        applyColorAdjustment(bitmap, 1.1f, 1.2f, 0.9f, intensity)
        
        return bitmap
    }
    
    private fun applySketchStyle(bitmap: Bitmap, styleData: JSONObject, intensity: Float): Bitmap {
        // Pencil or charcoal drawing style
        val canvas = Canvas(bitmap)
        
        // Convert to grayscale
        applyGrayscaleEffect(bitmap, intensity)
        
        // Apply sketch lines
        applySketchLines(canvas, bitmap, intensity)
        
        // Enhance edges
        applyEdgeEnhancement(bitmap, intensity * 1.5f)
        
        return bitmap
    }
    
    private fun applyAnimeStyle(bitmap: Bitmap, styleData: JSONObject, intensity: Float): Bitmap {
        // Japanese animation art style
        val canvas = Canvas(bitmap)
        
        // Apply cell shading
        applyCellShading(bitmap, intensity)
        
        // Enhance colors
        applyColorAdjustment(bitmap, 1.3f, 1.4f, 1.2f, intensity)
        
        // Simplify details
        applyDetailSimplification(bitmap, intensity)
        
        return bitmap
    }
    
    private fun applyVintageStyle(bitmap: Bitmap, styleData: JSONObject, intensity: Float): Bitmap {
        // Retro, aged photo effects
        val canvas = Canvas(bitmap)
        
        // Apply sepia tone
        applySepiaEffect(bitmap, intensity)
        
        // Add film grain
        applyFilmGrain(canvas, intensity)
        
        // Reduce saturation
        applyColorAdjustment(bitmap, 0.7f, 0.8f, 0.9f, intensity)
        
        return bitmap
    }
    
    private fun applyNoirStyle(bitmap: Bitmap, styleData: JSONObject, intensity: Float): Bitmap {
        // High contrast black and white, dramatic shadows
        val canvas = Canvas(bitmap)
        
        // Convert to high contrast B&W
        applyHighContrastBW(bitmap, intensity)
        
        // Enhance shadows and highlights
        applyShadowHighlightEnhancement(bitmap, intensity)
        
        return bitmap
    }
    
    // Helper methods for style effects
    private fun applySoftBlur(bitmap: Bitmap, radius: Float) {
        // Simple blur implementation
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        
        // Apply simple box blur
        val blurRadius = (radius * 2).toInt().coerceAtMost(10)
        for (i in 0 until blurRadius) {
            boxBlur(pixels, bitmap.width, bitmap.height, 1)
        }
        
        bitmap.setPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
    }
    
    private fun boxBlur(pixels: IntArray, width: Int, height: Int, radius: Int) {
        // Simple box blur implementation
        val temp = pixels.clone()
        
        for (y in 0 until height) {
            for (x in 0 until width) {
                var r = 0
                var g = 0
                var b = 0
                var count = 0
                
                for (dy in -radius..radius) {
                    for (dx in -radius..radius) {
                        val nx = (x + dx).coerceIn(0, width - 1)
                        val ny = (y + dy).coerceIn(0, height - 1)
                        val pixel = temp[ny * width + nx]
                        
                        r += (pixel shr 16) and 0xFF
                        g += (pixel shr 8) and 0xFF
                        b += pixel and 0xFF
                        count++
                    }
                }
                
                val avgR = r / count
                val avgG = g / count
                val avgB = b / count
                
                pixels[y * width + x] = (0xFF shl 24) or (avgR shl 16) or (avgG shl 8) or avgB
            }
        }
    }
    
    private fun applyColorAdjustment(bitmap: Bitmap, saturation: Float, contrast: Float, brightness: Float, intensity: Float) {
        val canvas = Canvas(bitmap)
        val paint = Paint()
        
        val colorMatrix = ColorMatrix().apply {
            setSaturation(1.0f + (saturation - 1.0f) * intensity)
            
            // Apply contrast and brightness
            val scale = contrast * intensity + (1.0f - intensity)
            val translate = (brightness - 1.0f) * intensity * 255
            
            postConcat(ColorMatrix(floatArrayOf(
                scale, 0f, 0f, 0f, translate,
                0f, scale, 0f, 0f, translate,
                0f, 0f, scale, 0f, translate,
                0f, 0f, 0f, 1f, 0f
            )))
        }
        
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
    
    private fun applyBrushTexture(canvas: Canvas, paint: Paint, intensity: Float) {
        // Simple brush texture overlay
        paint.alpha = (intensity * 50).toInt()
        
        for (i in 0 until (intensity * 100).toInt()) {
            val x = (Math.random() * canvas.width).toFloat()
            val y = (Math.random() * canvas.height).toFloat()
            val radius = (Math.random() * 5 + 2).toFloat()
            
            canvas.drawCircle(x, y, radius, paint)
        }
    }
    
    private fun applyBoldBrushStrokes(canvas: Canvas, intensity: Float) {
        val paint = Paint().apply {
            isAntiAlias = true
            strokeWidth = intensity * 8
            alpha = (intensity * 100).toInt()
        }
        
        // Draw random bold strokes
        for (i in 0 until (intensity * 20).toInt()) {
            val startX = (Math.random() * canvas.width).toFloat()
            val startY = (Math.random() * canvas.height).toFloat()
            val endX = startX + (Math.random() * 50 - 25).toFloat()
            val endY = startY + (Math.random() * 50 - 25).toFloat()
            
            canvas.drawLine(startX, startY, endX, endY, paint)
        }
    }
    
    private fun applyEdgeEnhancement(bitmap: Bitmap, intensity: Float) {
        // Simple edge detection and enhancement
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        
        val enhanced = pixels.clone()
        
        for (y in 1 until bitmap.height - 1) {
            for (x in 1 until bitmap.width - 1) {
                val center = pixels[y * bitmap.width + x]
                val right = pixels[y * bitmap.width + (x + 1)]
                val bottom = pixels[(y + 1) * bitmap.width + x]
                
                val edgeStrength = abs(getLuminance(center) - getLuminance(right)) +
                                 abs(getLuminance(center) - getLuminance(bottom))
                
                if (edgeStrength > 30) {
                    enhanced[y * bitmap.width + x] = enhancePixel(center, intensity)
                }
            }
        }
        
        bitmap.setPixels(enhanced, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
    }
    
    private fun getLuminance(pixel: Int): Int {
        val r = (pixel shr 16) and 0xFF
        val g = (pixel shr 8) and 0xFF
        val b = pixel and 0xFF
        return (0.299 * r + 0.587 * g + 0.114 * b).toInt()
    }
    
    private fun enhancePixel(pixel: Int, intensity: Float): Int {
        val r = (((pixel shr 16) and 0xFF) * (1 + intensity * 0.3)).toInt().coerceIn(0, 255)
        val g = (((pixel shr 8) and 0xFF) * (1 + intensity * 0.3)).toInt().coerceIn(0, 255)
        val b = ((pixel and 0xFF) * (1 + intensity * 0.3)).toInt().coerceIn(0, 255)
        return (0xFF shl 24) or (r shl 16) or (g shl 8) or b
    }
    
    // Additional helper methods for other effects
    private fun applyGeometricFragmentation(canvas: Canvas, bitmap: Bitmap, intensity: Float) {
        // Simplified geometric effect
        val paint = Paint().apply {
            isAntiAlias = true
            alpha = (intensity * 128).toInt()
            xfermode = PorterDuffXfermode(PorterDuff.Mode.OVERLAY)
        }
        
        val fragments = (intensity * 20).toInt()
        for (i in 0 until fragments) {
            val x = (Math.random() * canvas.width).toFloat()
            val y = (Math.random() * canvas.height).toFloat()
            val size = (Math.random() * 50 + 20).toFloat()
            
            canvas.drawRect(x, y, x + size, y + size, paint)
        }
    }
    
    private fun applyColorQuantization(bitmap: Bitmap, levels: Int, intensity: Float) {
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        
        val step = 256 / levels
        
        for (i in pixels.indices) {
            val pixel = pixels[i]
            val r = (pixel shr 16) and 0xFF
            val g = (pixel shr 8) and 0xFF
            val b = pixel and 0xFF
            
            val newR = ((r / step) * step * intensity + r * (1 - intensity)).toInt().coerceIn(0, 255)
            val newG = ((g / step) * step * intensity + g * (1 - intensity)).toInt().coerceIn(0, 255)
            val newB = ((b / step) * step * intensity + b * (1 - intensity)).toInt().coerceIn(0, 255)
            
            pixels[i] = (0xFF shl 24) or (newR shl 16) or (newG shl 8) or newB
        }
        
        bitmap.setPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
    }
    
    // Placeholder implementations for other effects
    private fun applyGlowEffect(canvas: Canvas, intensity: Float) { /* Implementation */ }
    private fun applyHalftonePattern(canvas: Canvas, intensity: Float) { /* Implementation */ }
    private fun applyColorFieldEffect(canvas: Canvas, bitmap: Bitmap, intensity: Float) { /* Implementation */ }
    private fun applyFormSimplification(bitmap: Bitmap, intensity: Float) { /* Implementation */ }
    private fun applyWatercolorBleeding(canvas: Canvas, bitmap: Bitmap, intensity: Float) { /* Implementation */ }
    private fun applyTransparencyEffect(bitmap: Bitmap, intensity: Float) { /* Implementation */ }
    private fun applyOilPaintTexture(canvas: Canvas, bitmap: Bitmap, intensity: Float) { /* Implementation */ }
    private fun applyGrayscaleEffect(bitmap: Bitmap, intensity: Float) { 
        val colorMatrix = ColorMatrix().apply { setSaturation(1.0f - intensity) }
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(colorMatrix) }
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
    private fun applySketchLines(canvas: Canvas, bitmap: Bitmap, intensity: Float) { /* Implementation */ }
    private fun applyCellShading(bitmap: Bitmap, intensity: Float) { /* Implementation */ }
    private fun applyDetailSimplification(bitmap: Bitmap, intensity: Float) { /* Implementation */ }
    private fun applySepiaEffect(bitmap: Bitmap, intensity: Float) {
        val sepiaMatrix = ColorMatrix(floatArrayOf(
            0.393f * intensity + (1 - intensity), 0.769f * intensity, 0.189f * intensity, 0f, 0f,
            0.349f * intensity, 0.686f * intensity + (1 - intensity), 0.168f * intensity, 0f, 0f,
            0.272f * intensity, 0.534f * intensity, 0.131f * intensity + (1 - intensity), 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        ))
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(sepiaMatrix) }
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
    private fun applyFilmGrain(canvas: Canvas, intensity: Float) { /* Implementation */ }
    private fun applyHighContrastBW(bitmap: Bitmap, intensity: Float) {
        applyGrayscaleEffect(bitmap, intensity)
        applyColorAdjustment(bitmap, 1.0f, 2.0f, 1.0f, intensity)
    }
    private fun applyShadowHighlightEnhancement(bitmap: Bitmap, intensity: Float) { /* Implementation */ }
    
    private fun saveProcessedImage(bitmap: Bitmap, styleName: String): Uri? {
        return try {
            val filename = "style_${styleName}_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, filename)
            
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, APIConfig.IMAGE_QUALITY, outputStream)
            outputStream.close()
            
            Uri.fromFile(file)
        } catch (e: Exception) {
            Log.e("StyleTransferProcessor", "Failed to save processed image", e)
            null
        }
    }
}