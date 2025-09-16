package com.example.superphoto.di

import com.example.superphoto.domain.ui.activities.MainActivity
import com.example.superphoto.domain.ui.fragment.HomeFragment
import com.example.superphoto.domain.ui.fragment.MineFragment
import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

val mainActivity = module {
    scope<MainActivity> {
        fragment { HomeFragment() }
        fragment { MineFragment() }
    }
}

val listModule = listOf(
    mainActivity,

)