package com.example.superphoto.di

import com.superphoto.ai.AIRepository
import com.superphoto.ai.GeminiApiService
import com.superphoto.ai.PollinationsApiService
import org.koin.dsl.module

val aiModule = module {
    // API Services
    single { GeminiApiService() }
    single { PollinationsApiService() }
    
    // AI Repository
    single { AIRepository(get(), get()) }
}