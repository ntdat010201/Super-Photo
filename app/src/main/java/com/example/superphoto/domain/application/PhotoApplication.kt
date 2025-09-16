package com.example.superphoto.domain.application

import android.app.Application
import com.example.superphoto.di.listModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PhotoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@PhotoApplication)
            modules(listModule)
        }
    }

}