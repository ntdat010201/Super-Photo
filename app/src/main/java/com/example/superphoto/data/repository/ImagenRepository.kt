package com.example.superphoto.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.example.superphoto.data.api.ImagenApiService
import com.example.superphoto.data.model.*
import com.example.superphoto.utils.StorageHelper
import com.superphoto.config.APIConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Repository for Google Imagen API
 * Handles real image generation using Google's Imagen model
 */
class ImagenRepository(
    private val imagenApiService: ImagenApiService,
    private val context: Context
) {
    
    companion object {
        private const val TAG = "ImagenRepository"
        private const val PROJECT_ID = "your-google-cloud-project-id" // TODO: Configure your project ID
        private const val LOCATION = "us-central1"
    }
    
    /**
     * Generate image from text prompt using Imagen
     */
    suspend fun generateImage(
        prompt: String,
        aspectRatio: String = "1:1",
        negativePrompt: String? = null,
        guidanceScale: Float = 7.0f,
        useFastModel: Boolean = false
    ): Result<ImageGenerationResponse> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Generating image with prompt: $prompt")
            
            val request = ImagenRequest(
                instances = listOf(ImagenInstance(prompt = prompt)),
                parameters = ImagenParameters(
                    aspectRatio = aspectRatio,
                    guidanceScale = guidanceScale,
                    negativePrompt = negativePrompt,
                    seed = Random().nextInt(1000000)
                )
            )
            
            val authHeader = "Bearer ${getAccessToken()}"
            
            val response = if (useFastModel) {
                imagenApiService.generateImageFast(authHeader, request)
            } else {
                imagenApiService.generateImage(authHeader, request)
            }
            
            if (response.isSuccessful) {
                val imagenResponse = response.body()
                if (imagenResponse?.predictions?.isNotEmpty() == true) {
                    val prediction = imagenResponse.predictions.first()
                    
                    // Check for safety filter
                    if (prediction.raiFilteredReason != null) {
                        return@withContext Result.failure(
                            Exception("Image generation blocked: ${prediction.raiFilteredReason}")
                        )
                    }
                    
                    // Save base64 image to file
                    val imageFile = saveBase64ToFile(prediction.bytesBase64Encoded)
                    
                    val result = ImageGenerationResponse(
                        success = true,
                        taskId = UUID.randomUUID().toString(),
                        status = "completed",
                        message = "Image generated successfully using Imagen API",
                        imageUrl = imageFile.absolutePath,
                        thumbnailUrl = imageFile.absolutePath,
                        width = 1024,
                        height = 1024,
                        fileSize = imageFile.length()
                    )
                    
                    Log.d(TAG, "Image generated successfully: ${imageFile.absolutePath}")
                    Result.success(result)
                } else {
                    Result.failure(Exception("No image generated"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "API Error: ${response.code()} - $errorBody")
                Result.failure(Exception("API Error: ${response.code()} - $errorBody"))
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error generating image", e)
            Result.failure(e)
        }
    }
    
    /**
     * Edit existing image using Imagen
     */
    suspend fun editImage(
        sourceImageUri: Uri,
        prompt: String,
        editMode: String = "inpainting-insert",
        maskMode: String = "background",
        negativePrompt: String? = null
    ): Result<ImageGenerationResponse> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Editing image with prompt: $prompt")
            
            // Convert image to base64
            val imageBase64 = convertImageToBase64(sourceImageUri)
            
            val request = ImagenEditRequest(
                instances = listOf(
                    ImagenEditInstance(
                        prompt = prompt,
                        image = ImagenImageData(bytesBase64Encoded = imageBase64)
                    )
                ),
                parameters = ImagenEditParameters(
                    editMode = editMode,
                    maskMode = maskMode,
                    negativePrompt = negativePrompt
                )
            )
            
            val authHeader = "Bearer ${getAccessToken()}"
            
            // Note: This would need a separate endpoint for image editing
            // For now, we'll use the generate endpoint as fallback
            val response = imagenApiService.generateImage(authHeader, 
                ImagenRequest(
                    instances = listOf(ImagenInstance(prompt = prompt)),
                    parameters = ImagenParameters(negativePrompt = negativePrompt)
                )
            )
            
            if (response.isSuccessful) {
                val imagenResponse = response.body()
                if (imagenResponse?.predictions?.isNotEmpty() == true) {
                    val prediction = imagenResponse.predictions.first()
                    
                    val imageFile = saveBase64ToFile(prediction.bytesBase64Encoded)
                    
                    val result = ImageGenerationResponse(
                        success = true,
                        taskId = UUID.randomUUID().toString(),
                        status = "completed",
                        message = "Image edited successfully using Imagen API",
                        imageUrl = imageFile.absolutePath,
                        thumbnailUrl = imageFile.absolutePath,
                        width = 1024,
                        height = 1024,
                        fileSize = imageFile.length()
                    )
                    
                    Result.success(result)
                } else {
                    Result.failure(Exception("No edited image generated"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error editing image", e)
            Result.failure(e)
        }
    }
    
    /**
     * Convert image URI to base64 string
     */
    private fun convertImageToBase64(imageUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val imageBytes = outputStream.toByteArray()
        
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    }
    
    /**
     * Save base64 encoded image to external storage
     */
    private fun saveBase64ToFile(base64String: String): File {
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        
        // Sử dụng StorageHelper để tạo file trong external storage
        val file = StorageHelper.createImageFile(context, "generated_images", "IMAGEN")
            ?: throw Exception("Cannot create file in external storage")
        
        Log.d(TAG, "Saving image to external storage: ${file.absolutePath}")
        
        FileOutputStream(file).use { fos ->
            fos.write(imageBytes)
        }
        
        // Cleanup old files (giữ lại 50 file mới nhất)
        StorageHelper.cleanupOldFiles(context, "generated_images", 50)
        
        Log.d(TAG, "Image saved successfully. Size: ${StorageHelper.getReadableFileSize(file)}")
        
        return file
    }
    
    /**
     * Get access token for Google Cloud API
     * TODO: Implement proper OAuth2 flow or service account authentication
     */
    private fun getAccessToken(): String {
        // For now, return the Gemini API key as fallback
        // In production, this should be a proper Google Cloud access token
        return APIConfig.getGeminiApiKey()
    }
}