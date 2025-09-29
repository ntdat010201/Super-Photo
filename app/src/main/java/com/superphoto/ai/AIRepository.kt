package com.superphoto.ai

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.superphoto.config.APIConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class AIRepository(
    private val geminiApiService: GeminiApiService,
    private val pollinationsApiService: PollinationsApiService
) {
    
    companion object {
        private const val TAG = "AIRepository"
    }
    
    /**
     * Tạo mô tả ảnh từ bitmap sử dụng Gemini AI
     */
    suspend fun generateImageDescription(bitmap: Bitmap, userPrompt: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val base64Image = bitmapToBase64(bitmap)
                val prompt = createImageDescriptionPrompt(userPrompt)
                
                val geminiRequest = GeminiRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(
                                GeminiPart(text = prompt),
                                GeminiPart(
                                    inlineData = GeminiInlineData(
                                        mimeType = "image/jpeg",
                                        data = base64Image
                                    )
                                )
                            )
                        )
                    ),
                    generationConfig = GeminiGenerationConfig(
                        temperature = APIConfig.TEMPERATURE,
                        maxOutputTokens = APIConfig.MAX_TOKENS
                    ),
                    safetySettings = listOf(
                        GeminiSafetySetting("HARM_CATEGORY_HARASSMENT", "BLOCK_MEDIUM_AND_ABOVE"),
                        GeminiSafetySetting("HARM_CATEGORY_HATE_SPEECH", "BLOCK_MEDIUM_AND_ABOVE"),
                        GeminiSafetySetting("HARM_CATEGORY_SEXUALLY_EXPLICIT", "BLOCK_MEDIUM_AND_ABOVE"),
                        GeminiSafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT", "BLOCK_MEDIUM_AND_ABOVE")
                    )
                )
                
                when (val result = geminiApiService.generateContent(geminiRequest)) {
                    is AIResult.Success -> {
                        val response = result.data
                        if (response.error != null) {
                            Result.failure(Exception("Gemini API error: ${response.error.message}"))
                        } else {
                            val description = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                                ?: "Unable to generate description"
                            Result.success(description)
                        }
                    }
                    is AIResult.Error -> {
                        Log.e(TAG, "Error generating image description: ${result.message}")
                        Result.failure(Exception(result.message))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error generating image description", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Tạo ảnh từ text sử dụng Pollinations AI
     */
    suspend fun generateImageFromText(prompt: String, aspectRatio: String = "1:1", style: String = "none"): Result<Bitmap> {
        return withContext(Dispatchers.IO) {
            try {
                val enhancedPrompt = createImageGenerationPrompt(prompt, style)
                val (width, height) = getAspectRatioDimensions(aspectRatio)
                
                val pollinationsRequest = PollinationsImageRequest(
                    prompt = enhancedPrompt,
                    width = width,
                    height = height,
                    model = "flux",
                    enhance = true
                )
                
                when (val result = pollinationsApiService.generateImage(pollinationsRequest)) {
                    is AIResult.Success -> {
                        Result.success(result.data)
                    }
                    is AIResult.Error -> {
                        Log.e(TAG, "Error generating image from text: ${result.message}")
                        Result.failure(Exception(result.message))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error generating image from text", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Chuyển đổi bitmap thành base64
     */
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
    
    /**
     * Tạo prompt cho việc mô tả ảnh
     */
    private fun createImageDescriptionPrompt(userPrompt: String): String {
        return """
            Analyze this portrait and create a detailed prompt for AI generation showing the SAME PERSON with: "$userPrompt"
            
            Describe:
            - Face: eye shape, nose, lips, jawline, skin tone
            - Hair: color, style, length
            - Expression and pose
            - Apply modification: "$userPrompt"
            - Keep same identity
            
            Format: "A [gender] with [facial features], [hair], [expression], [user modification], photorealistic portrait, same person"
            
            Respond with ONLY the prompt.
        """.trimIndent()
    }
    
    /**
     * Tạo prompt cho việc tạo ảnh
     */
    private fun createImageGenerationPrompt(userPrompt: String, style: String): String {
        val styleModifier = when (style.lowercase()) {
            "photo" -> "photorealistic, high quality photograph, professional photography, realistic, natural lighting"
            "anime" -> "anime style, manga style, Japanese animation art, same person identity"
            "illustration" -> "digital illustration, artistic illustration, detailed artwork, consistent facial features"
            "realistic" -> "photorealistic, high quality, professional photography, natural lighting"
            "artistic" -> "artistic style, creative interpretation, enhanced colors, same person identity"
            "vintage" -> "vintage style, retro aesthetic, film photography, consistent facial features"
            "modern" -> "modern style, clean aesthetic, contemporary, precise facial details"
            else -> "high quality, detailed, realistic, natural"
        }
        
        // Thêm các từ khóa để đảm bảo chất lượng và tính chính xác
        val qualityModifiers = "masterpiece, best quality, highly detailed, sharp focus, perfect lighting, high resolution"
        val consistencyModifiers = "consistent character, same person, maintain identity, preserve facial features, exact likeness, identical person, maintain appearance"
        
        return "$userPrompt, $consistencyModifiers, $styleModifier, $qualityModifiers"
    }
    
    /**
     * Lấy kích thước theo tỷ lệ khung hình
     */
    private fun getAspectRatioDimensions(aspectRatio: String): Pair<Int, Int> {
        return when (aspectRatio) {
            "1:1" -> Pair(1024, 1024)
            "16:9" -> Pair(1024, 576)
            "9:16" -> Pair(576, 1024)
            "3:4" -> Pair(768, 1024)
            "4:3" -> Pair(1024, 768)
            else -> Pair(1024, 1024) // Default to square
        }
    }
}