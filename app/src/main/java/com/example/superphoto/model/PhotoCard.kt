package com.example.superphoto.model

data class PhotoCard(
    val id: String,
    val title: String,
    val badge: String = "",
    val imageResource: Int? = null // Drawable resource ID for local images
)