package com.example.superphoto.models

data class PhotoItem(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val backgroundColor: String,
    val category: String,
    val isPopular: Boolean = false,
    val isHot: Boolean = false
)