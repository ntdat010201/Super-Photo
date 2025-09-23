package com.example.superphoto.di

import com.example.superphoto.data.api.GeminiApiService
import com.example.superphoto.data.repository.GeminiRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    // Gemini API Service
    single<GeminiApiService> {
        get<Retrofit>().create(GeminiApiService::class.java)
    }
    
    // Gemini Repository
    single {
        // TODO: Replace with your actual Gemini API key
        val apiKey = "YOUR_GEMINI_API_KEY_HERE"
        GeminiRepository(get(), apiKey)
    }
}