package com.example.superphoto.di

import com.example.superphoto.data.api.GeminiApiService
import com.example.superphoto.data.api.AIGenerationApiService
import com.example.superphoto.data.repository.GeminiRepository
import com.example.superphoto.data.repository.AIGenerationRepository
import com.example.superphoto.data.repository.AIGenerationFallbackRepository
import com.example.superphoto.data.repository.AIGenerationManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.superphoto.config.APIConfig

val networkModule = module {
    
    // OkHttp Client
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(APIConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(APIConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(APIConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
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
        try {
            val apiKey = APIConfig.getGeminiApiKey()
            GeminiRepository(get(), apiKey)
        } catch (e: IllegalStateException) {
            // Fallback for when API key is not configured
            GeminiRepository(get(), "NOT_CONFIGURED")
        }
    }
    
    // AI Generation Fallback Repository (Demo Mode)
    single<AIGenerationFallbackRepository> {
        AIGenerationFallbackRepository(get<GeminiRepository>(), androidContext())
    }
    
    // AI Generation Repository (with fallback support)
    single<AIGenerationRepository> {
        val apiKey = APIConfig.getAIGenerationApiKey()
        AIGenerationRepository(get(), androidContext(), apiKey)
    }
    
    // AI Generation Manager (Auto-selects between real API and fallback)
    single<AIGenerationManager> {
        AIGenerationManager(
            realRepository = get<AIGenerationRepository>(),
            fallbackRepository = get<AIGenerationFallbackRepository>(),
            context = androidContext()
        )
    }
}