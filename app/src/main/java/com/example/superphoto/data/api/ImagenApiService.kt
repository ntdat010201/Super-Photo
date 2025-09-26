package com.example.superphoto.data.api

import com.example.superphoto.data.model.ImagenRequest
import com.example.superphoto.data.model.ImagenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * API Service for Google Imagen (Vertex AI)
 * Generates real images using Google's Imagen model
 */
interface ImagenApiService {
    
    @POST("v1/projects/{project-id}/locations/us-central1/publishers/google/models/imagen-3.0-generate-001:predict")
    suspend fun generateImage(
        @Header("Authorization") authorization: String,
        @Body request: ImagenRequest
    ): Response<ImagenResponse>
    
    @POST("v1/projects/{project-id}/locations/us-central1/publishers/google/models/imagen-3.0-fast-generate-001:predict")
    suspend fun generateImageFast(
        @Header("Authorization") authorization: String,
        @Body request: ImagenRequest
    ): Response<ImagenResponse>
}