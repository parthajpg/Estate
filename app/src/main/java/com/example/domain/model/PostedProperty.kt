package com.example.domain.model

data class PostedProperty(
    val id: String,
    val title: String,
    val tagline: String,
    val category: String, // "Buy", "Rent", "Commercial", "New Projects"
    val city: String,
    val locality: String,
    val towerHouseNo: String,
    val bhk: Int,
    val carpetAreaSqFt: Int,
    val bathrooms: Int,
    val furnishing: String,
    val priceOrRent: Double,
    val priceFormatted: String,
    val description: String,
    val imageUrl: String,
    val ownerType: String,
    val ownerName: String,
    val ownerPhone: String,
    val ownerEmail: String,
    val isVerified: Boolean = false,
    val constructionStatus: String = "Ready to Move",
    val timestamp: Long = System.currentTimeMillis()
)
