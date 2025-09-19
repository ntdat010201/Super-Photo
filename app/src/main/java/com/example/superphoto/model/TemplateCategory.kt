package com.example.superphoto.model

data class TemplateCategory(
    val id: String,
    val name: String,
    val icon: String,
    val isSelected: Boolean = false
)