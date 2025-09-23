package com.example.superphoto.data.model

import com.google.gson.annotations.SerializedName

data class GeminiRequest(
    @SerializedName("contents")
    val contents: List<Content>,
    @SerializedName("generationConfig")
    val generationConfig: GenerationConfig? = null
)

data class Content(
    @SerializedName("parts")
    val parts: List<Part>
)

data class Part(
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("inline_data")
    val inlineData: InlineData? = null
)

data class InlineData(
    @SerializedName("mime_type")
    val mimeType: String,
    @SerializedName("data")
    val data: String
)

data class GenerationConfig(
    @SerializedName("temperature")
    val temperature: Float = 0.7f,
    @SerializedName("topK")
    val topK: Int = 40,
    @SerializedName("topP")
    val topP: Float = 0.95f,
    @SerializedName("maxOutputTokens")
    val maxOutputTokens: Int = 1024
)