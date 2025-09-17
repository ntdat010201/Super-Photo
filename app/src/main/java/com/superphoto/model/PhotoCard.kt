package com.superphoto.model

data class PhotoCard(
    val id: String,
    val title: String,
    val badge: String = "",
    val backgroundGradient: Int,
    val imageUrl: String? = null
)