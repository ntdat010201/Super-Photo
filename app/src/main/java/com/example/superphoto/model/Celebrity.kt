package com.example.superphoto.model

data class Celebrity(
    val id: String,
    val name: String,
    val imageUrl: String,
    val imageResource: Int? = null,
    val isSelected: Boolean = false
)