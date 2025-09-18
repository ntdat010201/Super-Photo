package com.example.superphoto.model

data class FeaturedCard(
    val id: String,
    val title: String,
    val badge: String,
    val imageResource: Int? = null
)