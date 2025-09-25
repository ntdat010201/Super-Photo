package com.example.superphoto.data.api

import com.example.superphoto.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AIGenerationApiService {
    
    // Image to Video Generation
    @Multipart
    @POST("api/v1/image-to-video")
    suspend fun generateVideoFromImages(
        @Header("Authorization") apiKey: String,
        @Part images: List<MultipartBody.Part>,
        @Part("prompt") prompt: RequestBody,
        @Part("negative_prompt") negativePrompt: RequestBody?,
        @Part("duration") duration: RequestBody,
        @Part("fps") fps: RequestBody = RequestBody.create(null, "24"),
        @Part("quality") quality: RequestBody = RequestBody.create(null, "high")
    ): Response<VideoGenerationResponse>
    
    // Text to Video Generation
    @POST("api/v1/text-to-video")
    suspend fun generateVideoFromText(
        @Header("Authorization") apiKey: String,
        @Body request: TextToVideoRequest
    ): Response<VideoGenerationResponse>
    
    // Lip Sync Generation
    @Multipart
    @POST("api/v1/lip-sync")
    suspend fun generateLipSync(
        @Header("Authorization") apiKey: String,
        @Part video: MultipartBody.Part,
        @Part audio: MultipartBody.Part,
        @Part("enhance_quality") enhanceQuality: RequestBody,
        @Part("preserve_expression") preserveExpression: RequestBody
    ): Response<VideoGenerationResponse>
    
    // AI Image Generation
    @Multipart
    @POST("api/v1/ai-images")
    suspend fun generateAIImage(
        @Header("Authorization") apiKey: String,
        @Part sourceImage: MultipartBody.Part?,
        @Part("prompt") prompt: RequestBody,
        @Part("aspect_ratio") aspectRatio: RequestBody,
        @Part("style") style: RequestBody,
        @Part("quality") quality: RequestBody = RequestBody.create(null, "high"),
        @Part("steps") steps: RequestBody = RequestBody.create(null, "50")
    ): Response<ImageGenerationResponse>
    
    // Check Generation Status
    @GET("api/v1/status/{taskId}")
    suspend fun checkGenerationStatus(
        @Header("Authorization") apiKey: String,
        @Path("taskId") taskId: String
    ): Response<GenerationStatusResponse>
    
    // Download Generated Content
    @GET("api/v1/download/{taskId}")
    suspend fun downloadGeneratedContent(
        @Header("Authorization") apiKey: String,
        @Path("taskId") taskId: String
    ): Response<DownloadResponse>
}