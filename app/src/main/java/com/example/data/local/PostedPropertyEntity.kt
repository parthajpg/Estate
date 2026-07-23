package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posted_properties")
data class PostedPropertyEntity(
    @PrimaryKey val id: String,
    val title: String,
    val tagline: String,
    val category: String, // "Buy", "Rent", "Commercial", "New Projects"
    val city: String,
    val locality: String,
    val towerHouseNo: String,
    val bhk: Int,
    val carpetAreaSqFt: Int,
    val bathrooms: Int,
    val furnishing: String, // "Unfurnished", "Semi-Furnished", "Fully Furnished"
    val priceOrRent: Double,
    val priceFormatted: String,
    val description: String,
    val imageUrl: String,
    val ownerType: String, // "Individual Owner" or "Broker / Dealer"
    val ownerName: String,
    val ownerPhone: String,
    val ownerEmail: String,
    val isVerified: Boolean = false,
    val constructionStatus: String = "Ready to Move",
    val timestamp: Long = System.currentTimeMillis()
)
