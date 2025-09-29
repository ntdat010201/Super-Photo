package com.superphoto.ai

import com.google.gson.annotations.SerializedName

// Gemini API Models
data class GeminiRequest(
    @SerializedName("contents")
    val contents: List<GeminiContent>,
    @SerializedName("generationConfig")
    val generationConfig: GeminiGenerationConfig? = null,
    @SerializedName("safetySettings")
    val safetySettings: List<GeminiSafetySetting>? = null
)

data class GeminiContent(
    @SerializedName("parts")
    val parts: List<GeminiPart>
)

data class GeminiPart(
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("inlineData")
    val inlineData: GeminiInlineData? = null
)

data class GeminiInlineData(
    @SerializedName("mimeType")
    val mimeType: String,
    @SerializedName("data")
    val data: String
)

data class GeminiGenerationConfig(
    @SerializedName("temperature")
    val temperature: Float = 0.7f,
    @SerializedName("topK")
    val topK: Int = 40,
    @SerializedName("topP")
    val topP: Float = 0.95f,
    @SerializedName("maxOutputTokens")
    val maxOutputTokens: Int = 1024
)

data class GeminiSafetySetting(
    @SerializedName("category")
    val category: String,
    @SerializedName("threshold")
    val threshold: String
)

data class GeminiResponse(
    @SerializedName("candidates")
    val candidates: List<GeminiCandidate>? = null,
    @SerializedName("error")
    val error: GeminiError? = null
)

data class GeminiCandidate(
    @SerializedName("content")
    val content: GeminiContent,
    @SerializedName("finishReason")
    val finishReason: String? = null,
    @SerializedName("safetyRatings")
    val safetyRatings: List<GeminiSafetyRating>? = null
)

data class GeminiSafetyRating(
    @SerializedName("category")
    val category: String,
    @SerializedName("probability")
    val probability: String
)

data class GeminiError(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

// Pollinations API Models (Simple URL-based, no complex models needed)
data class PollinationsImageRequest(
    val prompt: String,
    val width: Int = 1024,
    val height: Int = 1024,
    val seed: Int? = null,
    val model: String = "flux",
    val enhance: Boolean = true
)

// AI Repository Result Models
sealed class AIResult<out T> {
    data class Success<T>(val data: T) : AIResult<T>()
    data class Error(val message: String, val exception: Throwable? = null) : AIResult<Nothing>()
}

data class ImageDescription(
    val description: String,
    val enhancedPrompt: String
)

data class GeneratedImage(
    val bitmap: android.graphics.Bitmap,
    val prompt: String,
    val aspectRatio: String,
    val style: String
)