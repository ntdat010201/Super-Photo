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
            android.util.Log.d("GenerationStatusManager", "Starting download for taskId: $taskId, resultUrl: $resultUrl")
            
            // First try to get download info from API
            val downloadResult = repository.downloadGeneratedContent(taskId)
            
            downloadResult.fold(
                onSuccess = { downloadResponse ->
                    android.util.Log.d("GenerationStatusManager", "Download API success: ${downloadResponse.downloadUrl}")
                    val downloadUrl = downloadResponse.downloadUrl
                    val fileName = downloadResponse.fileName
                    downloadFile(downloadUrl, fileName, callback)
                },
                onFailure = { exception ->
                    android.util.Log.w("GenerationStatusManager", "Download API failed, using fallback: ${exception.message}")
                    // Fallback to direct download from result URL
                    val fileName = "generated_${taskId}_${System.currentTimeMillis()}.${getFileExtension(resultUrl)}"
                    downloadFile(resultUrl, fileName, callback)
                }
            )
        } catch (e: Exception) {
            android.util.Log.e("GenerationStatusManager", "Download failed", e)
            callback.onFailed("Download failed: ${e.message}")
        }
    }
    
    private suspend fun downloadFile(url: String, fileName: String, callback: StatusCallback) {
        try {
            android.util.Log.d("GenerationStatusManager", "Downloading file from URL: $url, fileName: $fileName")
            
            val buffer = if (url.startsWith("file://")) {
                // Handle local file URLs (demo mode)
                android.util.Log.d("GenerationStatusManager", "Processing local file URL")
                val localFilePath = url.removePrefix("file://")
                val localFile = File(localFilePath)
                
                if (localFile.exists()) {
                    android.util.Log.d("GenerationStatusManager", "Local file exists: ${localFile.absolutePath}, size: ${localFile.length()}")
                    localFile.readBytes()
                } else {
                    android.util.Log.e("GenerationStatusManager", "Local file does not exist: $localFilePath")
                    throw Exception("Local file not found: $localFilePath")
                }
            } else {
                // Handle remote URLs
                android.util.Log.d("GenerationStatusManager", "Processing remote URL")
                val connection = URL(url).openConnection()
                connection.connect()
                
                val inputStream = connection.getInputStream()
                val bytes = inputStream.readBytes()
                inputStream.close()
                bytes
            }
            
            android.util.Log.d("GenerationStatusManager", "Downloaded ${buffer.size} bytes")
            
            // Determine file type and save accordingly
            val fileExtension = getFileExtension(url)
            val subfolder = when (fileExtension) {
                "mp4" -> "ai_videos"
                "mp3" -> "ai_audio"
                else -> "ai_files"
            }
            
            android.util.Log.d("GenerationStatusManager", "File extension: $fileExtension, subfolder: $subfolder")
            
            val savedFile = if (fileExtension == "mp4") {
                // For video files, use StorageHelper to save to external storage
                android.util.Log.d("GenerationStatusManager", "Saving video using StorageHelper")
                val result = StorageHelper.saveVideoToExternalStorage(context, buffer, fileName, subfolder)
                android.util.Log.d("GenerationStatusManager", "StorageHelper result: $result")
                result
            } else {
                // For other files, save to Downloads folder
                android.util.Log.d("GenerationStatusManager", "Saving to Downloads folder")
                val downloadsDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SuperPhoto")
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }
                val file = File(downloadsDir, fileName)
                file.writeBytes(buffer)
                file
            }
            
            if (savedFile != null) {
                android.util.Log.d("GenerationStatusManager", "File saved successfully: ${savedFile.absolutePath}")
                val fileUri = Uri.fromFile(savedFile)
                callback.onCompleted(fileUri)
                Toast.makeText(context, "📁 File saved to: ${savedFile.absolutePath}", Toast.LENGTH_LONG).show()
            } else {
                android.util.Log.e("GenerationStatusManager", "savedFile is null")
                callback.onFailed("Failed to save file to external storage")
            }
            
        } catch (e: Exception) {
            android.util.Log.e("GenerationStatusManager", "File download failed", e)
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