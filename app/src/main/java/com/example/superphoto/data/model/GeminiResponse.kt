package com.example.superphoto.data.model

import com.google.gson.annotations.SerializedName

data class GeminiResponse(
    @SerializedName("candidates")
    val candidates: List<Candidate>?,
    @SerializedName("promptFeedback")
    val promptFeedback: PromptFeedback?,
    @SerializedName("error")
    val error: GeminiError?
)

data class Candidate(
    @SerializedName("content")
    val content: Content?,
    @SerializedName("finishReason")
    val finishReason: String?,
    @SerializedName("index")
    val index: Int?,
    @SerializedName("safetyRatings")
    val safetyRatings: List<SafetyRating>?
)

data class PromptFeedback(
    @SerializedName("safetyRatings")
    val safetyRatings: List<SafetyRating>?
)

data class SafetyRating(
    @SerializedName("category")
    val category: String?,
    @SerializedName("probability")
    val probability: String?
)

data class GeminiError(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?
)