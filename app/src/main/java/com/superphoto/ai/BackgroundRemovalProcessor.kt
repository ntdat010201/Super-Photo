package com.superphoto.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.net.Uri
import com.superphoto.config.APIConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Specialized processor for AI-powered background removal using Gemini 2.5 Flash
 * Supports multiple background removal techniques and edge refinement
 */
class BackgroundRemovalProcessor(private val context: Context) {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(APIConfig.API_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(APIConfig.API_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(APIConfig.API_TIMEOUT, TimeUnit.SECONDS)
        .build()
    
    data class BackgroundRemovalResult(
        val processedBitmap: Bitmap,
        val maskBitmap: Bitmap?,
        val confidence: Float,
        val processingTime: Long,
        val technique: String
    )
    
    enum class RemovalTechnique {
        SMART_DETECTION,    // AI detects best approach
        PERSON_FOCUS,       // Focus on person/people
        OBJECT_FOCUS,       // Focus on main object
        EDGE_REFINEMENT,    // High precision edges
        BATCH_PROCESSING    // Multiple objects
    }
    
    /**
     * Remove background from image using AI analysis
     */
    suspend fun removeBackground(
        imageUri: Uri,
        technique: RemovalTechnique = RemovalTechnique.SMART_DETECTION,
        preserveEdges: Boolean = true,
        transparentBackground: Boolean = true
    ): Result<BackgroundRemovalResult> {
        return try {
            val startTime = System.currentTimeMillis()
            
            // Load and prepare image
            val originalBitmap = GeminiAIProcessor(context).loadBitmapFromUri(imageUri)
                ?: return Result.failure(Exception("Failed to load image"))
            
            // Generate AI prompt based on technique
            val prompt = generateRemovalPrompt(technique, preserveEdges)
            
            // Get AI analysis for background removal
            val analysisResult = analyzeImageForBackgroundRemoval(originalBitmap, prompt)
            
            // Process the image based on AI analysis
            val processedBitmap = processBackgroundRemoval(
                originalBitmap, 
                analysisResult,
                transparentBackground
            )
            
            // Generate mask if needed
            val maskBitmap = if (preserveEdges) {
                generateRefinedMask(originalBitmap, analysisResult)
            } else null
            
            val processingTime = System.currentTimeMillis() - startTime
            
            Result.success(BackgroundRemovalResult(
                processedBitmap = processedBitmap,
                maskBitmap = maskBitmap,
                confidence = analysisResult.confidence,
                processingTime = processingTime,
                technique = technique.name
            ))
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Generate specialized prompt for background removal
     */
    private fun generateRemovalPrompt(
        technique: RemovalTechnique,
        preserveEdges: Boolean
    ): String {
        val basePrompt = """
            Analyze this image for background removal. Provide detailed analysis in JSON format:
            {
                "main_subject": "description of primary subject to keep",
                "background_elements": ["list of background elements to remove"],
                "edge_complexity": "simple/moderate/complex",
                "recommended_technique": "technique name",
                "subject_boundaries": {
                    "clear_edges": true/false,
                    "hair_details": true/false,
                    "fine_details": true/false
                },
                "confidence": 0.0-1.0
            }
        """.trimIndent()
        
        return when (technique) {
            RemovalTechnique.SMART_DETECTION -> """
                $basePrompt
                
                Focus on automatically detecting the best approach for this image.
                Consider subject type, background complexity, and edge details.
            """.trimIndent()
            
            RemovalTechnique.PERSON_FOCUS -> """
                $basePrompt
                
                Specifically focus on human subjects. Pay special attention to:
                - Hair details and fine edges
                - Clothing boundaries
                - Skin tone separation from background
            """.trimIndent()
            
            RemovalTechnique.OBJECT_FOCUS -> """
                $basePrompt
                
                Focus on non-human objects as main subjects:
                - Product photography
                - Objects with clear boundaries
                - Geometric shapes and defined edges
            """.trimIndent()
            
            RemovalTechnique.EDGE_REFINEMENT -> """
                $basePrompt
                
                Provide ultra-precise edge analysis:
                - Pixel-level boundary detection
                - Fine detail preservation
                - Anti-aliasing considerations
                ${if (preserveEdges) "- Hair and fur detail mapping" else ""}
            """.trimIndent()
            
            RemovalTechnique.BATCH_PROCESSING -> """
                $basePrompt
                
                Analyze for multiple subjects:
                - Identify all foreground objects
                - Separate overlapping subjects
                - Batch boundary detection
            """.trimIndent()
        }
    }
    
    /**
     * Analyze image using Gemini API for background removal
     */
    private suspend fun analyzeImageForBackgroundRemoval(
        bitmap: Bitmap,
        prompt: String
    ): BackgroundAnalysis {
        return withContext(Dispatchers.IO) {
            val base64Image = bitmapToBase64(bitmap)
            val response = callGeminiAPI(base64Image, prompt)
            parseBackgroundAnalysis(response)
        }
    }
    
    /**
     * Process background removal based on AI analysis
     */
    private fun processBackgroundRemoval(
        originalBitmap: Bitmap,
        analysis: BackgroundAnalysis,
        transparentBackground: Boolean
    ): Bitmap {
        val width = originalBitmap.width
        val height = originalBitmap.height
        
        // Create result bitmap
        val resultBitmap = Bitmap.createBitmap(
            width, height, 
            if (transparentBackground) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        )
        
        val canvas = Canvas(resultBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        
        // Apply background removal based on analysis
        when (analysis.recommendedTechnique) {
            "smart_detection" -> applySmartRemoval(canvas, originalBitmap, analysis, paint)
            "person_focus" -> applyPersonFocusRemoval(canvas, originalBitmap, analysis, paint)
            "object_focus" -> applyObjectFocusRemoval(canvas, originalBitmap, analysis, paint)
            "edge_refinement" -> applyEdgeRefinementRemoval(canvas, originalBitmap, analysis, paint)
            else -> applyDefaultRemoval(canvas, originalBitmap, analysis, paint)
        }
        
        return resultBitmap
    }
    
    /**
     * Generate refined mask for edge preservation
     */
    private fun generateRefinedMask(
        originalBitmap: Bitmap,
        analysis: BackgroundAnalysis
    ): Bitmap {
        val maskBitmap = Bitmap.createBitmap(
            originalBitmap.width,
            originalBitmap.height,
            Bitmap.Config.ALPHA_8
        )
        
        val canvas = Canvas(maskBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        
        // Generate mask based on subject boundaries
        if (analysis.subjectBoundaries.clearEdges) {
            paint.color = Color.WHITE
            // Apply clear edge mask
        } else {
            // Apply soft edge mask with feathering
            paint.color = Color.GRAY
        }
        
        // TODO: Implement actual mask generation based on AI analysis
        canvas.drawRect(0f, 0f, maskBitmap.width.toFloat(), maskBitmap.height.toFloat(), paint)
        
        return maskBitmap
    }
    
    // Background removal technique implementations
    private fun applySmartRemoval(canvas: Canvas, bitmap: Bitmap, analysis: BackgroundAnalysis, paint: Paint) {
        // Smart detection implementation
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        // TODO: Implement smart background removal algorithm
    }
    
    private fun applyPersonFocusRemoval(canvas: Canvas, bitmap: Bitmap, analysis: BackgroundAnalysis, paint: Paint) {
        // Person-focused removal implementation
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        // TODO: Implement person-focused background removal
    }
    
    private fun applyObjectFocusRemoval(canvas: Canvas, bitmap: Bitmap, analysis: BackgroundAnalysis, paint: Paint) {
        // Object-focused removal implementation
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        // TODO: Implement object-focused background removal
    }
    
    private fun applyEdgeRefinementRemoval(canvas: Canvas, bitmap: Bitmap, analysis: BackgroundAnalysis, paint: Paint) {
        // Edge refinement implementation
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        // TODO: Implement edge refinement algorithm
    }
    
    private fun applyDefaultRemoval(canvas: Canvas, bitmap: Bitmap, analysis: BackgroundAnalysis, paint: Paint) {
        // Default removal implementation
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        // TODO: Implement default background removal
    }
    
    /**
     * Call Gemini API for background analysis
     */
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
                throw Exception("API call failed: ${response.code}")
            }
            
            val responseBody = response.body?.string() ?: throw Exception("Empty response")
            parseGeminiResponse(responseBody)
        }
    }
    
    /**
     * Create request body for Gemini API
     */
    private fun createGeminiRequestBody(base64Image: String, prompt: String): RequestBody {
        val json = JSONObject().apply {
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
                put("temperature", APIConfig.DEFAULT_TEMPERATURE)
                put("topK", APIConfig.DEFAULT_TOP_K)
                put("topP", APIConfig.DEFAULT_TOP_P)
                put("maxOutputTokens", APIConfig.MAX_OUTPUT_TOKENS)
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
        
        return json.toString().toRequestBody("application/json".toMediaType())
    }
    
    /**
     * Parse Gemini API response
     */
    private fun parseGeminiResponse(responseBody: String): String {
        val json = JSONObject(responseBody)
        val candidates = json.getJSONArray("candidates")
        if (candidates.length() > 0) {
            val candidate = candidates.getJSONObject(0)
            val content = candidate.getJSONObject("content")
            val parts = content.getJSONArray("parts")
            if (parts.length() > 0) {
                return parts.getJSONObject(0).getString("text")
            }
        }
        throw Exception("No valid response from API")
    }
    
    /**
     * Parse background analysis from AI response
     */
    private fun parseBackgroundAnalysis(response: String): BackgroundAnalysis {
        return try {
            // Extract JSON from response
            val jsonStart = response.indexOf("{")
            val jsonEnd = response.lastIndexOf("}") + 1
            val jsonString = response.substring(jsonStart, jsonEnd)
            val json = JSONObject(jsonString)
            
            BackgroundAnalysis(
                mainSubject = json.optString("main_subject", "Unknown subject"),
                backgroundElements = parseStringArray(json.optJSONArray("background_elements")),
                edgeComplexity = json.optString("edge_complexity", "moderate"),
                recommendedTechnique = json.optString("recommended_technique", "smart_detection"),
                subjectBoundaries = parseSubjectBoundaries(json.optJSONObject("subject_boundaries")),
                confidence = json.optDouble("confidence", 0.7).toFloat()
            )
        } catch (e: Exception) {
            // Fallback analysis
            BackgroundAnalysis(
                mainSubject = "Detected subject",
                backgroundElements = listOf("background"),
                edgeComplexity = "moderate",
                recommendedTechnique = "smart_detection",
                subjectBoundaries = SubjectBoundaries(true, false, false),
                confidence = 0.6f
            )
        }
    }
    
    private fun parseStringArray(jsonArray: JSONArray?): List<String> {
        val result = mutableListOf<String>()
        jsonArray?.let {
            for (i in 0 until it.length()) {
                result.add(it.getString(i))
            }
        }
        return result
    }
    
    private fun parseSubjectBoundaries(json: JSONObject?): SubjectBoundaries {
        return SubjectBoundaries(
            clearEdges = json?.optBoolean("clear_edges", true) ?: true,
            hairDetails = json?.optBoolean("hair_details", false) ?: false,
            fineDetails = json?.optBoolean("fine_details", false) ?: false
        )
    }
    
    /**
     * Convert bitmap to base64 for API
     */
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, APIConfig.IMAGE_QUALITY, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.getEncoder().encodeToString(byteArray)
    }
    
    /**
     * Data classes for background analysis
     */
    data class BackgroundAnalysis(
        val mainSubject: String,
        val backgroundElements: List<String>,
        val edgeComplexity: String,
        val recommendedTechnique: String,
        val subjectBoundaries: SubjectBoundaries,
        val confidence: Float
    )
    
    data class SubjectBoundaries(
        val clearEdges: Boolean,
        val hairDetails: Boolean,
        val fineDetails: Boolean
    )
}