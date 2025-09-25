package com.example.superphoto.data.model

import com.google.gson.annotations.SerializedName

// Text to Video Request
data class TextToVideoRequest(
    @SerializedName("prompt")
    val prompt: String,
    @SerializedName("negative_prompt")
    val negativePrompt: String? = null,
    @SerializedName("duration")
    val duration: Int, // in seconds
    @SerializedName("fps")
    val fps: Int = 24,
    @SerializedName("quality")
    val quality: String = "high", // high, medium, low
    @SerializedName("style")
    val style: String? = null
)

// Video Generation Response
data class VideoGenerationResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("task_id")
    val taskId: String,
    @SerializedName("status")
    val status: String, // pending, processing, completed, failed
    @SerializedName("message")
    val message: String,
    @SerializedName("estimated_time")
    val estimatedTime: Int? = null, // in seconds
    @SerializedName("video_url")
    val videoUrl: String? = null,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String? = null,
    @SerializedName("duration")
    val duration: Float? = null,
    @SerializedName("file_size")
    val fileSize: Long? = null
)

// Image Generation Response
data class ImageGenerationResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("task_id")
    val taskId: String,
    @SerializedName("status")
    val status: String, // pending, processing, completed, failed
    @SerializedName("message")
    val message: String,
    @SerializedName("estimated_time")
    val estimatedTime: Int? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String? = null,
    @SerializedName("width")
    val width: Int? = null,
    @SerializedName("height")
    val height: Int? = null,
    @SerializedName("file_size")
    val fileSize: Long? = null
)

// Generation Status Response
data class GenerationStatusResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("task_id")
    val taskId: String,
    @SerializedName("status")
    val status: String, // pending, processing, completed, failed
    @SerializedName("progress")
    val progress: Int, // 0-100
    @SerializedName("message")
    val message: String,
    @SerializedName("result_url")
    val resultUrl: String? = null,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String? = null,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("completed_at")
    val completedAt: String? = null
)

// Download Response
data class DownloadResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("download_url")
    val downloadUrl: String,
    @SerializedName("file_name")
    val fileName: String,
    @SerializedName("file_size")
    val fileSize: Long,
    @SerializedName("content_type")
    val contentType: String,
    @SerializedName("expires_at")
    val expiresAt: String
)

// Error Response
data class ErrorResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("error")
    val error: String,
    @SerializedName("error_code")
    val errorCode: String? = null,
    @SerializedName("details")
    val details: Map<String, Any>? = null
)

// Generation Task Status Enum
enum class GenerationStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED
}

// Quality Options
enum class QualityOption(val value: String) {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high"),
    ULTRA("ultra")
}

// Video Duration Options
enum class VideoDuration(val seconds: Int, val displayName: String) {
    SHORT(10, "10s"),
    MEDIUM(15, "15s"),
    LONG(20, "20s")
}

// Aspect Ratio Options
enum class AspectRatio(val value: String, val displayName: String) {
    SQUARE("1:1", "1:1"),
    LANDSCAPE("16:9", "16:9"),
    PORTRAIT("9:16", "9:16"),
    PHOTO("3:4", "3:4")
}

// Style Options
enum class StyleOption(val value: String, val displayName: String) {
    NONE("none", "None"),
    PHOTO("photo", "Photo"),
    ANIME("anime", "Anime"),
    ILLUSTRATION("illustration", "Illustration"),
    ARTISTIC("artistic", "Artistic"),
    CINEMATIC("cinematic", "Cinematic")
}