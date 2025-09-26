package com.example.superphoto.data.repository

import android.content.Context
import android.net.Uri
import com.example.superphoto.data.model.*
import com.superphoto.config.APIConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Manager class that automatically chooses between real AI Generation API and Gemini fallback
 * based on configuration and availability
 */
class AIGenerationManager(
    private val realRepository: AIGenerationRepository,
    private val fallbackRepository: AIGenerationFallbackRepository,
    private val context: Context
) {
    
    companion object {
        private const val TAG = "AIGenerationManager"
    }
    
    private fun shouldUseFallback(): Boolean {
        return APIConfig.isDemoMode()
    }
    
    // Image to Video Generation
    suspend fun generateVideoFromImages(
        imageUris: List<Uri>,
        prompt: String,
        negativePrompt: String? = null,
        duration: Int = 10
    ): Result<VideoGenerationResponse> = withContext(Dispatchers.IO) {
        return@withContext if (shouldUseFallback()) {
            fallbackRepository.generateVideoFromImages(imageUris, prompt, negativePrompt, duration)
        } else {
            realRepository.generateVideoFromImages(imageUris, prompt, negativePrompt, duration)
        }
    }
    
    // Text to Video Generation
    suspend fun generateVideoFromText(
        prompt: String,
        negativePrompt: String? = null,
        duration: Int = 10,
        style: String? = null
    ): Result<VideoGenerationResponse> = withContext(Dispatchers.IO) {
        return@withContext if (shouldUseFallback()) {
            fallbackRepository.generateVideoFromText(prompt, negativePrompt, duration, style)
        } else {
            realRepository.generateVideoFromText(prompt, negativePrompt, duration, style)
        }
    }
    
    // Lip Sync Generation
    suspend fun generateLipSync(
        videoUri: Uri,
        audioUri: Uri,
        enhanceQuality: Boolean = false,
        preserveExpression: Boolean = true
    ): Result<VideoGenerationResponse> = withContext(Dispatchers.IO) {
        return@withContext if (shouldUseFallback()) {
            fallbackRepository.generateLipSync(videoUri, audioUri, enhanceQuality, preserveExpression)
        } else {
            realRepository.generateLipSync(videoUri, audioUri, enhanceQuality, preserveExpression)
        }
    }
    
    // AI Image Generation
    suspend fun generateAIImage(
        sourceImageUri: Uri?,
        prompt: String,
        aspectRatio: String = "1:1",
        style: String = "none"
    ): Result<ImageGenerationResponse> = withContext(Dispatchers.IO) {
        return@withContext if (shouldUseFallback()) {
            fallbackRepository.generateAIImage(sourceImageUri, prompt, aspectRatio, style)
        } else {
            realRepository.generateAIImage(sourceImageUri, prompt, aspectRatio, style)
        }
    }
    
    // Check Generation Status
    suspend fun checkGenerationStatus(taskId: String): Result<GenerationStatusResponse> = withContext(Dispatchers.IO) {
        return@withContext if (shouldUseFallback()) {
            fallbackRepository.checkGenerationStatus(taskId)
        } else {
            realRepository.checkGenerationStatus(taskId)
        }
    }
    
    // Download Generated Content
    suspend fun downloadGeneratedContent(taskId: String): Result<DownloadResponse> = withContext(Dispatchers.IO) {
        return@withContext if (shouldUseFallback()) {
            fallbackRepository.downloadGeneratedContent(taskId)
        } else {
            realRepository.downloadGeneratedContent(taskId)
        }
    }
    
    // Get current mode info
    fun getCurrentMode(): String {
        return if (shouldUseFallback()) {
            "Demo Mode (Using Gemini AI for simulation)"
        } else {
            "Production Mode (Using real AI Generation APIs)"
        }
    }
    
    // Check if demo mode is active
    fun isDemoMode(): Boolean {
        return shouldUseFallback()
    }
}