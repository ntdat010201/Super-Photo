package com.superphoto.utils

import com.superphoto.config.APIConfig
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicLong

/**
 * Rate Limit Manager for Gemini API Free Tier
 * Manages request timing to stay within free tier limits
 */
object RateLimitManager {
    
    private val lastRequestTime = AtomicLong(0)
    private val requestCount = AtomicLong(0)
    private val windowStartTime = AtomicLong(System.currentTimeMillis())
    
    /**
     * Wait if necessary to respect rate limits
     */
    suspend fun waitForRateLimit() {
        val currentTime = System.currentTimeMillis()
        val timeSinceLastRequest = currentTime - lastRequestTime.get()
        
        // Reset counter every minute
        if (currentTime - windowStartTime.get() >= 60000) {
            requestCount.set(0)
            windowStartTime.set(currentTime)
        }
        
        // Check if we've hit the rate limit
        if (requestCount.get() >= APIConfig.FREE_TIER_FLASH_RPM) {
            val waitTime = 60000 - (currentTime - windowStartTime.get())
            if (waitTime > 0) {
                delay(waitTime)
                requestCount.set(0)
                windowStartTime.set(System.currentTimeMillis())
            }
        }
        
        // Ensure minimum delay between requests
        if (timeSinceLastRequest < APIConfig.REQUEST_DELAY_MS) {
            delay(APIConfig.REQUEST_DELAY_MS - timeSinceLastRequest)
        }
        
        lastRequestTime.set(System.currentTimeMillis())
        requestCount.incrementAndGet()
    }
    
    /**
     * Check if we can make a request without waiting
     */
    fun canMakeRequest(): Boolean {
        val currentTime = System.currentTimeMillis()
        
        // Reset counter every minute
        if (currentTime - windowStartTime.get() >= 60000) {
            return true
        }
        
        return requestCount.get() < APIConfig.FREE_TIER_FLASH_RPM
    }
    
    /**
     * Get remaining requests in current window
     */
    fun getRemainingRequests(): Int {
        val currentTime = System.currentTimeMillis()
        
        // Reset counter every minute
        if (currentTime - windowStartTime.get() >= 60000) {
            return APIConfig.FREE_TIER_FLASH_RPM
        }
        
        return (APIConfig.FREE_TIER_FLASH_RPM - requestCount.get()).toInt().coerceAtLeast(0)
    }
    
    /**
     * Get time until next request window
     */
    fun getTimeUntilReset(): Long {
        val currentTime = System.currentTimeMillis()
        val windowTime = currentTime - windowStartTime.get()
        return if (windowTime >= 60000) 0 else 60000 - windowTime
    }
}