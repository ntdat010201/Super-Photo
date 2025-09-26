package com.example.superphoto.data.api

import com.example.superphoto.data.model.GeminiRequest
import com.example.superphoto.data.model.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    
    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
    
    @POST("v1beta/models/gemini-2.5-pro:generateContent")
    suspend fun generateContentPro(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}