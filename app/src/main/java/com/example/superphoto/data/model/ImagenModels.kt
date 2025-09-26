package com.example.superphoto.data.model

import com.google.gson.annotations.SerializedName

/**
 * Request model for Imagen API
 */
data class ImagenRequest(
    @SerializedName("instances")
    val instances: List<ImagenInstance>,
    @SerializedName("parameters")
    val parameters: ImagenParameters
)

data class ImagenInstance(
    @SerializedName("prompt")
    val prompt: String
)

data class ImagenParameters(
    @SerializedName("sampleCount")
    val sampleCount: Int = 1,
    @SerializedName("aspectRatio")
    val aspectRatio: String = "1:1", // "1:1", "9:16", "16:9", "4:3", "3:4"
    @SerializedName("safetyFilterLevel")
    val safetyFilterLevel: String = "block_some",
    @SerializedName("personGeneration")
    val personGeneration: String = "allow_adult",
    @SerializedName("includeRaiReason")
    val includeRaiReason: Boolean = false,
    @SerializedName("seed")
    val seed: Int? = null,
    @SerializedName("guidanceScale")
    val guidanceScale: Float = 7.0f,
    @SerializedName("negativePrompt")
    val negativePrompt: String? = null
)

/**
 * Response model for Imagen API
 */
data class ImagenResponse(
    @SerializedName("predictions")
    val predictions: List<ImagenPrediction>
)

data class ImagenPrediction(
    @SerializedName("bytesBase64Encoded")
    val bytesBase64Encoded: String,
    @SerializedName("mimeType")
    val mimeType: String,
    @SerializedName("raiFilteredReason")
    val raiFilteredReason: String? = null
)

/**
 * Enhanced request for image-to-image generation
 */
data class ImagenEditRequest(
    @SerializedName("instances")
    val instances: List<ImagenEditInstance>,
    @SerializedName("parameters")
    val parameters: ImagenEditParameters
)

data class ImagenEditInstance(
    @SerializedName("prompt")
    val prompt: String,
    @SerializedName("image")
    val image: ImagenImageData
)

data class ImagenImageData(
    @SerializedName("bytesBase64Encoded")
    val bytesBase64Encoded: String
)

data class ImagenEditParameters(
    @SerializedName("sampleCount")
    val sampleCount: Int = 1,
    @SerializedName("editMode")
    val editMode: String = "inpainting-insert", // "inpainting-insert", "inpainting-remove", "outpainting", "product-image"
    @SerializedName("maskMode")
    val maskMode: String = "background", // "background", "foreground", "semantic"
    @SerializedName("guidanceScale")
    val guidanceScale: Float = 7.0f,
    @SerializedName("safetyFilterLevel")
    val safetyFilterLevel: String = "block_some",
    @SerializedName("personGeneration")
    val personGeneration: String = "allow_adult",
    @SerializedName("negativePrompt")
    val negativePrompt: String? = null
)