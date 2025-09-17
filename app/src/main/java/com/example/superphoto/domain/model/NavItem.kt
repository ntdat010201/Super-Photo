package com.example.superphoto.domain.model

data class NavItem(
    val id: String,
    val title: String,
    val iconRes: Int,
    val isSelected: Boolean = false
)