package com.example.superphoto.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.superphoto.ui.activities.MainActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object StorageHelper {
    private const val TAG = "StorageHelper"
    private const val APP_FOLDER = "SuperPhoto"

    /**
     * Kiểm tra quyền lưu trữ
     */
    private fun hasStoragePermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            true // No permission needed for MediaStore on Android 13+
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    /**
     * Tạo thư mục SuperPhoto trong Pictures của external storage
     */
    fun createAppDirectory(context: Context): File? {
        return try {
            // Kiểm tra external storage có sẵn không
            if (!isExternalStorageWritable()) {
                Log.e(TAG, "External storage is not writable")
                return null
            }
            
            // Tạo thư mục Pictures/SuperPhoto
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val appDir = File(picturesDir, APP_FOLDER)
            
            if (!appDir.exists()) {
                val created = appDir.mkdirs()
                if (created) {
                    Log.d(TAG, "Created app directory: ${appDir.absolutePath}")
                } else {
                    Log.e(TAG, "Failed to create app directory")
                    return null
                }
            }
            
            appDir
        } catch (e: Exception) {
            Log.e(TAG, "Error creating app directory", e)
            null
        }
    }
    
    /**
     * Tạo thư mục con trong SuperPhoto (ví dụ: generated_images, edited_images)
     */
    fun createSubDirectory(context: Context, subDirName: String): File? {
        val appDir = createAppDirectory(context) ?: return null
        
        return try {
            val subDir = File(appDir, subDirName)
            if (!subDir.exists()) {
                val created = subDir.mkdirs()
                if (created) {
                    Log.d(TAG, "Created sub directory: ${subDir.absolutePath}")
                } else {
                    Log.e(TAG, "Failed to create sub directory: $subDirName")
                    return null
                }
            }
            subDir
        } catch (e: Exception) {
            Log.e(TAG, "Error creating sub directory: $subDirName", e)
            null
        }
    }
    
    /**
     * Tạo file ảnh với tên unique trong thư mục được chỉ định
     */
    fun createImageFile(context: Context, subDirName: String, prefix: String = "IMG"): File? {
        val subDir = createSubDirectory(context, subDirName) ?: return null
        
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "${prefix}_${timeStamp}.jpg"
            val imageFile = File(subDir, fileName)
            
            Log.d(TAG, "Created image file path: ${imageFile.absolutePath}")
            imageFile
        } catch (e: Exception) {
            Log.e(TAG, "Error creating image file", e)
            null
        }
    }
    
    /**
     * Kiểm tra external storage có thể ghi được không
     */
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
    
    /**
     * Kiểm tra external storage có thể đọc được không
     */
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in setOf(
            Environment.MEDIA_MOUNTED,
            Environment.MEDIA_MOUNTED_READ_ONLY
        )
    }
    
    /**
     * Lấy đường dẫn thư mục Pictures/SuperPhoto
     */
    fun getAppDirectoryPath(): String {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File(picturesDir, APP_FOLDER).absolutePath
    }
    
    /**
     * Lấy kích thước file theo định dạng readable
     */
    fun getReadableFileSize(file: File): String {
        val bytes = file.length()
        return when {
            bytes >= 1024 * 1024 -> String.format("%.1f MB", bytes / (1024.0 * 1024.0))
            bytes >= 1024 -> String.format("%.1f KB", bytes / 1024.0)
            else -> "$bytes B"
        }
    }
    
    /**
     * Xóa file cũ nếu thư mục có quá nhiều file (giữ lại 50 file mới nhất)
     */
    fun cleanupOldFiles(context: Context, subDirName: String, maxFiles: Int = 50) {
        try {
            val subDir = File(createAppDirectory(context), subDirName)
            if (!subDir.exists()) return
            
            val files = subDir.listFiles()?.filter { it.isFile } ?: return
            
            if (files.size > maxFiles) {
                // Sắp xếp theo thời gian sửa đổi (cũ nhất trước)
                val sortedFiles = files.sortedBy { it.lastModified() }
                val filesToDelete = sortedFiles.take(files.size - maxFiles)
                
                filesToDelete.forEach { file ->
                    if (file.delete()) {
                        Log.d(TAG, "Deleted old file: ${file.name}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning up old files", e)
        }
    }
    
    /**
     * Lưu video vào bộ nhớ ngoài (tương thích Android 10+)
     */
    fun saveVideoToExternalStorage(
        context: Context,
        videoData: ByteArray,
        fileName: String,
        subDirName: String = "videos"
    ): File? {
        return try {
            // Kiểm tra quyền lưu trữ trước khi lưu
            if (!hasStoragePermission(context)) {
                Log.e(TAG, "Storage permission not granted")
                return null
            }

            // Sử dụng MediaStore cho Android 10+ (API 29+)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                saveVideoUsingMediaStore(context, videoData, fileName, subDirName)
            } else {
                // Sử dụng cách cũ cho Android 9 trở xuống
                saveVideoLegacy(context, videoData, fileName, subDirName)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving video to external storage", e)
            null
        }
    }
    
    /**
     * Lưu video sử dụng MediaStore (Android 10+)
     */
    @androidx.annotation.RequiresApi(android.os.Build.VERSION_CODES.Q)
    private fun saveVideoUsingMediaStore(
        context: Context,
        videoData: ByteArray,
        fileName: String,
        subDirName: String
    ): File? {
        val resolver = context.contentResolver
        val contentValues = android.content.ContentValues().apply {
            put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, 
                "${Environment.DIRECTORY_MOVIES}/$APP_FOLDER/$subDirName")
        }
        
        val uri = resolver.insert(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
        
        return uri?.let { videoUri ->
            resolver.openOutputStream(videoUri)?.use { outputStream ->
                outputStream.write(videoData)
                Log.d(TAG, "Video saved successfully using MediaStore: $videoUri")
                
                // Trả về File object để tương thích với code hiện tại
                val cursor = resolver.query(videoUri, arrayOf(android.provider.MediaStore.Video.Media.DATA), null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val columnIndex = it.getColumnIndex(android.provider.MediaStore.Video.Media.DATA)
                        if (columnIndex >= 0) {
                            val filePath = it.getString(columnIndex)
                            File(filePath)
                        } else null
                    } else null
                }
            }
        }
    }
    
    /**
     * Lưu video cách cũ (Android 9 trở xuống)
     */
    private fun saveVideoLegacy(
        context: Context,
        videoData: ByteArray,
        fileName: String,
        subDirName: String
    ): File? {
        if (!isExternalStorageWritable()) {
            Log.e(TAG, "External storage is not writable")
            return null
        }
        
        val subDir = createSubDirectory(context, subDirName) ?: return null
        val videoFile = File(subDir, fileName)
        
        videoFile.writeBytes(videoData)
        
        // Scan file để hiển thị trong gallery
        MediaScannerConnection.scanFile(
            context,
            arrayOf(videoFile.absolutePath),
            arrayOf("video/*"),
            null
        )
        
        Log.d(TAG, "Video saved successfully (legacy): ${videoFile.absolutePath}")
        return videoFile
    }
    
    /**
     * Lưu ảnh vào bộ nhớ ngoài (tương thích Android 10+)
     */
    fun saveImageToExternalStorage(
        context: Context,
        bitmap: Bitmap,
        filename: String,
        subfolder: String = "images"
    ): File? {
        return try {
            // Kiểm tra quyền lưu trữ trước khi lưu
            if (!hasStoragePermission(context)) {
                Log.e(TAG, "Storage permission not granted")
                return null
            }

            // Sử dụng MediaStore cho Android 10+ (API 29+)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                saveImageUsingMediaStore(context, bitmap, filename, subfolder)
            } else {
                // Sử dụng cách cũ cho Android 9 trở xuống
                saveImageLegacy(context, bitmap, filename, subfolder)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving image to external storage", e)
            null
        }
    }
    
    /**
     * Lưu ảnh sử dụng MediaStore (Android 10+)
     */
    @androidx.annotation.RequiresApi(android.os.Build.VERSION_CODES.Q)
    private fun saveImageUsingMediaStore(
        context: Context,
        bitmap: Bitmap,
        filename: String,
        subfolder: String
    ): File? {
        val resolver = context.contentResolver
        val contentValues = android.content.ContentValues().apply {
            put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, 
                "${Environment.DIRECTORY_PICTURES}/$APP_FOLDER/$subfolder")
        }
        
        val uri = resolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        
        return uri?.let { imageUri ->
            resolver.openOutputStream(imageUri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                Log.d(TAG, "Image saved successfully using MediaStore: $imageUri")
                
                // Trả về File object để tương thích với code hiện tại
                val cursor = resolver.query(imageUri, arrayOf(android.provider.MediaStore.Images.Media.DATA), null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val columnIndex = it.getColumnIndex(android.provider.MediaStore.Images.Media.DATA)
                        if (columnIndex >= 0) {
                            val filePath = it.getString(columnIndex)
                            File(filePath)
                        } else null
                    } else null
                }
            }
        }
    }
    
    /**
     * Lưu ảnh cách cũ (Android 9 trở xuống)
     */
    private fun saveImageLegacy(
        context: Context,
        bitmap: Bitmap,
        filename: String,
        subfolder: String
    ): File? {
        if (!isExternalStorageWritable()) {
            Log.e(TAG, "External storage is not writable")
            return null
        }
        
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val appDir = File(picturesDir, APP_FOLDER)
        val subDir = File(appDir, subfolder)
        
        if (!subDir.exists()) {
            subDir.mkdirs()
        }
        
        val imageFile = File(subDir, filename)
        val outputStream = imageFile.outputStream()
        
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.close()
        
        // Scan file để hiển thị trong gallery
        MediaScannerConnection.scanFile(
            context,
            arrayOf(imageFile.absolutePath),
            arrayOf("image/*"),
            null
        )
        
        Log.d(TAG, "Image saved successfully (legacy): ${imageFile.absolutePath}")
        return imageFile
    }
}