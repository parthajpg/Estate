package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inquiries")
data class InquiryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val propertyId: String,
    val propertyTitle: String,
    val userName: String,
    val userPhone: String,
    val userEmail: String,
    val message: String,
    val preferredTime: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Pending"
)
