package com.superphoto.model

data class FeaturedCard(
    val id: String,
    val title: String,
    val description: String,
    val badge: String,
    val backgroundGradient: Int,
    val imageUrl: String? = null
)