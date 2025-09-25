package com.example.superphoto.data.repository

import android.content.Context
import android.net.Uri
import com.example.superphoto.data.api.AIGenerationApiService
import com.example.superphoto.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AIGenerationRepository(
    private val apiService: AIGenerationApiService,
    private val context: Context,
    private val apiKey: String
) {
    
    companion object {
        private const val TAG = "AIGenerationRepository"
    }
    
    // Image to Video Generation
    suspend fun generateVideoFromImages(
        imageUris: List<Uri>,
        prompt: String,
        negativePrompt: String? = null,
        duration: Int = 10
    ): Result<VideoGenerationResponse> = withContext(Dispatchers.IO) {
        try {
            val imageParts = imageUris.mapNotNull { uri ->
                createMultipartFromUri(uri, "images")
            }
            
            val promptBody = prompt.toRequestBody("text/plain".toMediaTypeOrNull())
            val negativePromptBody = negativePrompt?.toRequestBody("text/plain".toMediaTypeOrNull())
            val durationBody = duration.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            
            val response = apiService.generateVideoFromImages(
                apiKey = "Bearer $apiKey",
                images = imageParts,
                prompt = promptBody,
                negativePrompt = negativePromptBody,
                duration = durationBody
            )
            
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Text to Video Generation
    suspend fun generateVideoFromText(
        prompt: String,
        negativePrompt: String? = null,
        duration: Int = 10,
        style: String? = null
    ): Result<VideoGenerationResponse> = withContext(Dispatchers.IO) {
        try {
            val request = TextToVideoRequest(
                prompt = prompt,
                negativePrompt = negativePrompt,
                duration = duration,
                style = style
            )
            
            val response = apiService.generateVideoFromText(
                apiKey = "Bearer $apiKey",
                request = request
            )
            
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Lip Sync Generation
    suspend fun generateLipSync(
        videoUri: Uri,
        audioUri: Uri,
        enhanceQuality: Boolean = false,
        preserveExpression: Boolean = true
    ): Result<VideoGenerationResponse> = withContext(Dispatchers.IO) {
        try {
            val videoPart = createMultipartFromUri(videoUri, "video")
            val audioPart = createMultipartFromUri(audioUri, "audio")
            
            if (videoPart == null || audioPart == null) {
                return@withContext Result.failure(Exception("Failed to process video or audio file"))
            }
            
            val enhanceQualityBody = enhanceQuality.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val preserveExpressionBody = preserveExpression.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            
            val response = apiService.generateLipSync(
                apiKey = "Bearer $apiKey",
                video = videoPart,
                audio = audioPart,
                enhanceQuality = enhanceQualityBody,
                preserveExpression = preserveExpressionBody
            )
            
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // AI Image Generation
    suspend fun generateAIImage(
        sourceImageUri: Uri?,
        prompt: String,
        aspectRatio: String = "1:1",
        style: String = "none"
    ): Result<ImageGenerationResponse> = withContext(Dispatchers.IO) {
        try {
            val sourceImagePart = sourceImageUri?.let { createMultipartFromUri(it, "source_image") }
            val promptBody = prompt.toRequestBody("text/plain".toMediaTypeOrNull())
            val aspectRatioBody = aspectRatio.toRequestBody("text/plain".toMediaTypeOrNull())
            val styleBody = style.toRequestBody("text/plain".toMediaTypeOrNull())
            
            val response = apiService.generateAIImage(
                apiKey = "Bearer $apiKey",
                sourceImage = sourceImagePart,
                prompt = promptBody,
                aspectRatio = aspectRatioBody,
                style = styleBody
            )
            
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Check Generation Status
    suspend fun checkGenerationStatus(taskId: String): Result<GenerationStatusResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.checkGenerationStatus(
                apiKey = "Bearer $apiKey",
                taskId = taskId
            )
            
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Download Generated Content
    suspend fun downloadGeneratedContent(taskId: String): Result<DownloadResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.downloadGeneratedContent(
                apiKey = "Bearer $apiKey",
                taskId = taskId
            )
            
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Helper function to create MultipartBody.Part from Uri
    private fun createMultipartFromUri(uri: Uri, partName: String): MultipartBody.Part? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}")
            
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            
            val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
            val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
            
            MultipartBody.Part.createFormData(partName, file.name, requestBody)
        } catch (e: Exception) {
            null
        }
    }
    
    // Generic response handler
    private fun <T> handleResponse(response: Response<T>): Result<T> {
        return if (response.isSuccessful) {
            response.body()?.let { body ->
                Result.success(body)
            } ?: Result.failure(Exception("Empty response body"))
        } else {
            val errorMessage = response.errorBody()?.string() ?: "Unknown error"
            Result.failure(Exception("API Error: ${response.code()} - $errorMessage"))
        }
    }
}