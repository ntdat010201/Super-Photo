package com.example.superphoto.di

import com.example.superphoto.data.api.GeminiApiService
import com.example.superphoto.data.api.AIGenerationApiService
import com.example.superphoto.data.repository.GeminiRepository
import com.example.superphoto.data.repository.AIGenerationRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    
    // OkHttp Client
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    
    // Retrofit for Gemini API
    single(qualifier = org.koin.core.qualifier.named("gemini")) {
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    // Retrofit for AI Generation API
    single(qualifier = org.koin.core.qualifier.named("ai_generation")) {
        Retrofit.Builder()
            .baseUrl("https://api.superphoto.ai/") // TODO: Replace with actual AI generation API base URL
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    // Gemini API Service
    single<GeminiApiService> {
        get<Retrofit>(qualifier = org.koin.core.qualifier.named("gemini")).create(GeminiApiService::class.java)
    }
    
    // AI Generation API Service
    single<AIGenerationApiService> {
        get<Retrofit>(qualifier = org.koin.core.qualifier.named("ai_generation")).create(AIGenerationApiService::class.java)
    }
    
    // Gemini Repository
    single {
        // TODO: Replace with your actual Gemini API key
        val apiKey = "YOUR_GEMINI_API_KEY_HERE"
        GeminiRepository(get(), apiKey)
    }
    
    // AI Generation Repository
    single {
        // TODO: Replace with your actual AI Generation API key
        val apiKey = "YOUR_AI_GENERATION_API_KEY_HERE"
        AIGenerationRepository(get(), androidContext(), apiKey)
    }
}