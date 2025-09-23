package com.example.superphoto.di

import com.example.superphoto.ui.activities.MainActivity
import org.koin.dsl.module

val mainActivity = module {
    scope<MainActivity> {
    }
}

val listModule = listOf(
    mainActivity,
    networkModule,
)