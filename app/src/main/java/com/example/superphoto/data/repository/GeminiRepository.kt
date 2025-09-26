package com.example.superphoto.data.repository

import android.graphics.Bitmap
import android.util.Base64
import com.example.superphoto.data.api.GeminiApiService
import com.example.superphoto.data.model.*
import java.io.ByteArrayOutputStream

class GeminiRepository(
    private val apiService: GeminiApiService,
    private val apiKey: String
) {
    
    suspend fun generateCelebrityPhoto(
        userPhoto: Bitmap,
        celebrityName: String
    ): Result<String> {
        return try {
            val base64Image = bitmapToBase64(userPhoto)
            
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(
                                text = createCelebrityPrompt(celebrityName)
                            ),
                            Part(
                                inlineData = InlineData(
                                    mimeType = "image/jpeg",
                                    data = base64Image
                                )
                            )
                        )
                    )
                ),
                generationConfig = GenerationConfig(
                    temperature = 0.8f,
                    maxOutputTokens = 2048
                )
            )
            
            val response = apiService.generateContent(apiKey, request)
            
            if (response.isSuccessful) {
                val geminiResponse = response.body()
                if (geminiResponse?.error != null) {
                    Result.failure(Exception("API Error: ${geminiResponse.error.message}"))
                } else {
                    val generatedText = geminiResponse?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (generatedText != null) {
                        Result.success(generatedText)
                    } else {
                        Result.failure(Exception("No content generated"))
                    }
                }
            } else {
                Result.failure(Exception("HTTP Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun generateContent(prompt: String): Result<String> {
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(text = prompt)
                        )
                    )
                ),
                generationConfig = GenerationConfig(
                    temperature = 0.8f,
                    maxOutputTokens = 2048
                )
            )
            
            val response = apiService.generateContent(apiKey, request)
            
            if (response.isSuccessful) {
                val geminiResponse = response.body()
                if (geminiResponse?.error != null) {
                    Result.failure(Exception("API Error: ${geminiResponse.error.message}"))
                } else {
                    val generatedText = geminiResponse?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (generatedText != null) {
                        Result.success(generatedText)
                    } else {
                        Result.failure(Exception("No content generated"))
                    }
                }
            } else {
                Result.failure(Exception("HTTP Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun generateContentWithImage(
        prompt: String, 
        imageUri: android.net.Uri, 
        context: android.content.Context
    ): Result<String> {
        return try {
            // Convert URI to Bitmap first
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            
            if (bitmap == null) {
                return Result.failure(Exception("Could not decode image from URI"))
            }
            
            val base64Image = bitmapToBase64(bitmap)
            
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(text = prompt),
                            Part(
                                inlineData = InlineData(
                                    mimeType = "image/jpeg",
                                    data = base64Image
                                )
                            )
                        )
                    )
                ),
                generationConfig = GenerationConfig(
                    temperature = 0.8f,
                    maxOutputTokens = 2048
                )
            )
            
            val response = apiService.generateContent(apiKey, request)
            
            if (response.isSuccessful) {
                val geminiResponse = response.body()
                if (geminiResponse?.error != null) {
                    Result.failure(Exception("API Error: ${geminiResponse.error.message}"))
                } else {
                    val generatedText = geminiResponse?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (generatedText != null) {
                        Result.success(generatedText)
                    } else {
                        Result.failure(Exception("No content generated"))
                    }
                }
            } else {
                Result.failure(Exception("HTTP Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun createCelebrityPrompt(celebrityName: String): String {
        return """
            Analyze this photo and provide detailed instructions for transforming the person's appearance to look like $celebrityName.
            
            Focus on:
            1. Facial features that need to be adjusted
            2. Hair style and color changes
            3. Makeup and styling suggestions
            4. Overall appearance modifications
            
            Provide specific, actionable guidance for photo editing to achieve the celebrity look.
            Keep the response concise but detailed enough for implementation.
        """.trimIndent()
    }
    
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}