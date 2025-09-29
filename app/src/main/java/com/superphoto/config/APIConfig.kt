package com.superphoto.config

/**
 * API Configuration for SuperPhoto
 * Manages API keys and endpoints for AI services
 */
object APIConfig {
    
    // Gemini AI Configuration
    const val GEMINI_API_KEY = "AIzaSyAKz9L1LMv9hNuTfPxx5wuJbTwEdtShIYY" // API key configured
    const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"
    const val GEMINI_MODEL = "gemini-2.5-flash" // Latest model for free tier
    const val GEMINI_MODEL_PRO = "gemini-2.5-pro" // Latest pro model for complex tasks
    
    // Free Tier Limits (per minute)
    const val FREE_TIER_FLASH_RPM = 15 // Gemini 2.5 Flash
    const val FREE_TIER_PRO_RPM = 5   // Gemini 2.5 Pro
    
    // AI Generation API Configuration
    const val AI_GENERATION_API_KEY = "DEMO_MODE" // Using Gemini as fallback for demo
    const val AI_GENERATION_BASE_URL = "https://generativelanguage.googleapis.com" // Using Gemini for demo
    
    // Pollinations AI Configuration (Free Image Generation)
    const val POLLINATIONS_BASE_URL = "https://image.pollinations.ai"
    const val POLLINATIONS_API_KEY = "FREE" // Pollinations is free to use
    
    // API Endpoints
    const val GEMINI_GENERATE_CONTENT = "$GEMINI_BASE_URL/$GEMINI_MODEL:generateContent"
    const val GEMINI_GENERATE_CONTENT_PRO = "$GEMINI_BASE_URL/$GEMINI_MODEL_PRO:generateContent"
    
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
    const val API_TIMEOUT = 45000L // 45 seconds timeout (increased for free tier)
    const val CONNECT_TIMEOUT_SECONDS = 30L // Connection timeout in seconds
    const val READ_TIMEOUT_SECONDS = 90L // Read timeout in seconds (increased for free tier)
    
    // AI Repository Configuration
    const val GEMINI_ENDPOINT = "/generateContent"
    const val MAX_TOKENS = 1024
    const val TEMPERATURE = 0.7f
    
    // Rate Limiting Configuration
    const val REQUEST_DELAY_MS = 4000L // 4 seconds between requests for free tier
    const val MAX_RETRIES = 3 // Maximum retry attempts
    const val RETRY_DELAY_MS = 2000L // Delay between retries
    
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
    const val ENABLE_DEMO_MODE = true // Use Gemini as fallback for video/image generation
    
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
     * Get full API URL with key (Flash model)
     */
    fun getGeminiUrl(): String {
        return "$GEMINI_GENERATE_CONTENT?key=${getGeminiApiKey()}"
    }
    
    /**
     * Get full API URL with key (Pro model for complex tasks)
     */
    fun getGeminiProUrl(): String {
        return "$GEMINI_GENERATE_CONTENT_PRO?key=${getGeminiApiKey()}"
    }
    
    /**
     * Get appropriate model URL based on task complexity
     */
    fun getModelUrl(useProModel: Boolean = false): String {
        return if (useProModel) getGeminiProUrl() else getGeminiUrl()
    }
    
    // Demo Mode Helper Functions
    fun isDemoMode(): Boolean {
        return ENABLE_DEMO_MODE && AI_GENERATION_API_KEY == "DEMO_MODE"
    }
    
    fun getAIGenerationApiKey(): String {
        return if (isDemoMode()) GEMINI_API_KEY else AI_GENERATION_API_KEY
    }
    
    fun getAIGenerationBaseUrl(): String {
        return if (isDemoMode()) GEMINI_BASE_URL else AI_GENERATION_BASE_URL
    }
}