package com.example.domain.model

data class Inquiry(
    val id: Long = 0,
    val propertyId: String,
    val propertyTitle: String,
    val userName: String,
    val userPhone: String,
    val userEmail: String,
    val message: String,
    val preferredTime: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Submitted"
)
