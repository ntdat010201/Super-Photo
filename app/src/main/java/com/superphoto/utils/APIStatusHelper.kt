package com.superphoto.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.superphoto.config.APIConfig

/**
 * Helper class for API status and configuration guidance
 */
object APIStatusHelper {
    
    /**
     * Check API configuration and show appropriate message
     */
    fun checkAndShowAPIStatus(context: Context): Boolean {
        return if (APIConfig.isConfigured()) {
            showAPIReadyMessage(context)
            true
        } else {
            showAPIConfigurationDialog(context)
            false
        }
    }
    
    /**
     * Show API ready message with rate limit info
     */
    private fun showAPIReadyMessage(context: Context) {
        val remainingRequests = RateLimitManager.getRemainingRequests()
        val message = "✅ Gemini API sẵn sàng!\n" +
                "🔄 Còn lại: $remainingRequests requests\n" +
                "⏱️ Free tier: ${APIConfig.FREE_TIER_FLASH_RPM} requests/phút"
        
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    
    /**
     * Show API configuration dialog with instructions
     */
    private fun showAPIConfigurationDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("🔑 Cấu hình Gemini API")
            .setMessage(getConfigurationInstructions())
            .setPositiveButton("Hiểu rồi") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Hướng dẫn chi tiết") { dialog, _ ->
                showDetailedInstructions(context)
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
    
    /**
     * Show detailed configuration instructions
     */
    private fun showDetailedInstructions(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("📋 Hướng dẫn chi tiết")
            .setMessage(getDetailedInstructions())
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    /**
     * Get basic configuration instructions
     */
    private fun getConfigurationInstructions(): String {
        return """
            🚀 Để sử dụng tính năng AI, bạn cần:
            
            1️⃣ Truy cập: https://aistudio.google.com/
            2️⃣ Đăng nhập với Google
            3️⃣ Nhấn "Get API Key"
            4️⃣ Tạo API key miễn phí
            5️⃣ Thay thế trong APIConfig.kt
            
            💡 Hoàn toàn MIỄN PHÍ!
            ⚡ 15 requests/phút với Gemini 2.5 Flash
        """.trimIndent()
    }
    
    /**
     * Get detailed configuration instructions
     */
    private fun getDetailedInstructions(): String {
        return """
            📝 Chi tiết cấu hình:
            
            🔧 File: APIConfig.kt
            📍 Dòng: const val GEMINI_API_KEY = "..."
            
            🔄 Thay thế:
            "YOUR_GEMINI_API_KEY_HERE"
            ↓
            "your_actual_api_key_here"
            
            ✅ Sau khi cấu hình:
            • Khởi động lại ứng dụng
            • Tất cả tính năng AI sẽ hoạt động
            • Style Transfer, Face Swap, AI Enhance...
            
            🎯 Free Tier Limits:
            • Gemini 2.5 Flash: 15 RPM
            • Gemini 2.5 Pro: 5 RPM
            • Không cần thẻ tín dụng
        """.trimIndent()
    }
    
    /**
     * Show rate limit status
     */
    fun showRateLimitStatus(context: Context) {
        val remainingRequests = RateLimitManager.getRemainingRequests()
        val timeUntilReset = RateLimitManager.getTimeUntilReset()
        
        val message = if (remainingRequests > 0) {
            "✅ Còn lại: $remainingRequests requests\n" +
            "⏱️ Reset sau: ${timeUntilReset / 1000}s"
        } else {
            "⏳ Đã hết quota\n" +
            "🔄 Reset sau: ${timeUntilReset / 1000}s"
        }
        
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    
    /**
     * Check if can make request and show appropriate message
     */
    fun checkCanMakeRequest(context: Context): Boolean {
        return if (!APIConfig.isConfigured()) {
            showAPIConfigurationDialog(context)
            false
        } else if (!RateLimitManager.canMakeRequest()) {
            showRateLimitStatus(context)
            false
        } else {
            true
        }
    }
}