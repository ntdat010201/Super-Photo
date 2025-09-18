package com.example.superphoto.di

import com.superphoto.ui.activities.MainActivity
import com.superphoto.ui.fragment.AssetsFragment
import com.superphoto.ui.fragment.HomeFragment
import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

val mainActivity = module {
    scope<MainActivity> {
        fragment { AssetsFragment() }
        fragment { HomeFragment() }
    }
}

val listModule = listOf(
    mainActivity,
)