package com.example.superphoto.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.superphoto.data.repository.AIGenerationManager
import com.example.superphoto.data.model.GenerationStatusResponse
import com.example.superphoto.data.model.DownloadResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class GenerationStatusManager(
    private val context: Context,
    private val repository: AIGenerationManager,
    private val lifecycleScope: LifecycleCoroutineScope
) {
    
    companion object {
        private const val POLLING_INTERVAL = 3000L // 3 seconds
        private const val MAX_POLLING_ATTEMPTS = 60 // 3 minutes max
    }
    
    interface StatusCallback {
        fun onProgress(progress: Int, message: String)
        fun onCompleted(resultUri: Uri?)
        fun onFailed(error: String)
    }
    
    fun startStatusPolling(taskId: String, callback: StatusCallback) {
        lifecycleScope.launch {
            var attempts = 0
            
            while (attempts < MAX_POLLING_ATTEMPTS) {
                try {
                    val result = repository.checkGenerationStatus(taskId)
                    
                    result.fold(
                        onSuccess = { statusResponse ->
                            when (statusResponse.status.lowercase()) {
                                "pending", "processing" -> {
                                    callback.onProgress(statusResponse.progress, statusResponse.message)
                                    delay(POLLING_INTERVAL)
                                    attempts++
                                }
                                "completed" -> {
                                    if (statusResponse.resultUrl != null) {
                                        downloadResult(statusResponse.resultUrl, taskId, callback)
                                    } else {
                                        callback.onCompleted(null)
                                    }
                                    return@launch
                                }
                                "failed" -> {
                                    callback.onFailed(statusResponse.error ?: "Generation failed")
                                    return@launch
                                }
                                else -> {
                                    callback.onFailed("Unknown status: ${statusResponse.status}")
                                    return@launch
                                }
                            }
                        },
                        onFailure = { exception ->
                            if (attempts >= MAX_POLLING_ATTEMPTS - 1) {
                                callback.onFailed("Status check failed: ${exception.message}")
                                return@launch
                            }
                            delay(POLLING_INTERVAL)
                            attempts++
                        }
                    )
                } catch (e: Exception) {
                    if (attempts >= MAX_POLLING_ATTEMPTS - 1) {
                        callback.onFailed("Status polling error: ${e.message}")
                        return@launch
                    }
                    delay(POLLING_INTERVAL)
                    attempts++
                }
            }
            
            // Timeout
            callback.onFailed("Generation timeout - please check status manually")
        }
    }
    
    private suspend fun downloadResult(resultUrl: String, taskId: String, callback: StatusCallback) {
        try {
            // First try to get download info from API
            val downloadResult = repository.downloadGeneratedContent(taskId)
            
            downloadResult.fold(
                onSuccess = { downloadResponse ->
                    val downloadUrl = downloadResponse.downloadUrl
                    val fileName = downloadResponse.fileName
                    downloadFile(downloadUrl, fileName, callback)
                },
                onFailure = {
                    // Fallback to direct download from result URL
                    val fileName = "generated_${taskId}_${System.currentTimeMillis()}.${getFileExtension(resultUrl)}"
                    downloadFile(resultUrl, fileName, callback)
                }
            )
        } catch (e: Exception) {
            callback.onFailed("Download failed: ${e.message}")
        }
    }
    
    private suspend fun downloadFile(url: String, fileName: String, callback: StatusCallback) {
        try {
            // Download file to external storage using StorageHelper
            val connection = URL(url).openConnection()
            connection.connect()
            
            val inputStream = connection.getInputStream()
            val buffer = inputStream.readBytes()
            inputStream.close()
            
            // Determine file type and save accordingly
            val fileExtension = getFileExtension(url)
            val subfolder = when (fileExtension) {
                "mp4" -> "ai_videos"
                "mp3" -> "ai_audio"
                else -> "ai_files"
            }
            
            val savedFile = if (fileExtension == "mp4") {
                // For video files, use StorageHelper to save to external storage
                StorageHelper.saveVideoToExternalStorage(context, buffer, fileName, subfolder)
            } else {
                // For other files, save to Downloads folder
                val downloadsDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SuperPhoto")
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }
                val file = File(downloadsDir, fileName)
                file.writeBytes(buffer)
                file
            }
            
            if (savedFile != null) {
                val fileUri = Uri.fromFile(savedFile)
                callback.onCompleted(fileUri)
                Toast.makeText(context, "📁 File saved to: ${savedFile.absolutePath}", Toast.LENGTH_LONG).show()
            } else {
                callback.onFailed("Failed to save file to external storage")
            }
            
        } catch (e: Exception) {
            callback.onFailed("File download failed: ${e.message}")
        }
    }
    
    private fun getFileExtension(url: String): String {
        return when {
            url.contains("video", ignoreCase = true) || url.contains(".mp4") -> "mp4"
            url.contains("image", ignoreCase = true) || url.contains(".jpg") || url.contains(".png") -> "jpg"
            url.contains("audio", ignoreCase = true) || url.contains(".mp3") -> "mp3"
            else -> "bin"
        }
    }
}