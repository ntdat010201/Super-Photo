package com.superphoto.config

/**
 * API Configuration for SuperPhoto
 * Manages API keys and endpoints for AI services
 */
object APIConfig {
    
    // Gemini AI Configuration
    const val GEMINI_API_KEY = "YOUR_GEMINI_API_KEY_HERE" // TODO: Replace with actual API key
    const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"
    const val GEMINI_MODEL = "gemini-2.0-flash-exp"
    
    // AI Generation API Configuration
    const val AI_GENERATION_API_KEY = "YOUR_AI_GENERATION_API_KEY_HERE" // TODO: Replace with actual API key
    const val AI_GENERATION_BASE_URL = "https://api.superphoto.ai" // TODO: Replace with actual base URL
    
    // API Endpoints
    const val GEMINI_GENERATE_CONTENT = "$GEMINI_BASE_URL/$GEMINI_MODEL:generateContent"
    
    // AI Generation Endpoints
    const val IMAGE_TO_VIDEO_ENDPOINT = "$AI_GENERATION_BASE_URL/api/v1/image-to-video"
    const val TEXT_TO_VIDEO_ENDPOINT = "$AI_GENERATION_BASE_URL/api/v1/text-to-video"
    const val LIP_SYNC_ENDPOINT = "$AI_GENERATION_BASE_URL/api/v1/lip-sync"
    const val AI_IMAGES_ENDPOINT = "$AI_GENERATION_BASE_URL/api/v1/ai-images"
    const val STATUS_ENDPOINT = "$AI_GENERATION_BASE_URL/api/v1/status"
    const val DOWNLOAD_ENDPOINT = "$AI_GENERATION_BASE_URL/api/v1/download"
    
    // Request Configuration
    const val MAX_IMAGE_SIZE = 1024 // Max width/height for image processing
    const val IMAGE_QUALITY = 80 // JPEG compression quality (0-100)
    const val API_TIMEOUT = 30000L // 30 seconds timeout
    const val CONNECT_TIMEOUT_SECONDS = 30L // Connection timeout in seconds
    const val READ_TIMEOUT_SECONDS = 60L // Read timeout in seconds
    
    // Processing Configuration
    const val DEFAULT_TEMPERATURE = 0.7f
    const val DEFAULT_TOP_K = 40
    const val DEFAULT_TOP_P = 0.95f
    const val MAX_OUTPUT_TOKENS = 1024
    
    // Generation Configuration (for Gemini API)
    const val GENERATION_CONFIG_TEMPERATURE = DEFAULT_TEMPERATURE
    const val GENERATION_CONFIG_TOP_K = DEFAULT_TOP_K
    const val GENERATION_CONFIG_TOP_P = DEFAULT_TOP_P
    const val GENERATION_CONFIG_MAX_OUTPUT_TOKENS = MAX_OUTPUT_TOKENS
    const val WRITE_TIMEOUT_SECONDS = 60L // Write timeout in seconds
    
    // Safety Settings
    val SAFETY_SETTINGS = mapOf(
        "HARM_CATEGORY_HARASSMENT" to "BLOCK_MEDIUM_AND_ABOVE",
        "HARM_CATEGORY_HATE_SPEECH" to "BLOCK_MEDIUM_AND_ABOVE",
        "HARM_CATEGORY_SEXUALLY_EXPLICIT" to "BLOCK_MEDIUM_AND_ABOVE",
        "HARM_CATEGORY_DANGEROUS_CONTENT" to "BLOCK_MEDIUM_AND_ABOVE"
    )
    
    // Feature Flags
    const val ENABLE_AI_PROCESSING = true
    const val ENABLE_OFFLINE_MODE = false
    const val ENABLE_CACHE = true
    
    // Cache Configuration
    const val CACHE_SIZE_MB = 50L
    const val CACHE_DURATION_HOURS = 24L
    
    /**
     * Check if API is properly configured
     */
    fun isConfigured(): Boolean {
        return GEMINI_API_KEY != "YOUR_GEMINI_API_KEY_HERE" && 
               GEMINI_API_KEY.isNotBlank()
    }
    
    /**
     * Get API key with validation
     */
    fun getGeminiApiKey(): String {
        if (!isConfigured()) {
            throw IllegalStateException("Gemini API key not configured. Please set GEMINI_API_KEY in APIConfig.")
        }
        return GEMINI_API_KEY
    }
    
    /**
     * Get full API URL with key
     */
    fun getGeminiUrl(): String {
        return "$GEMINI_GENERATE_CONTENT?key=${getGeminiApiKey()}"
    }
}