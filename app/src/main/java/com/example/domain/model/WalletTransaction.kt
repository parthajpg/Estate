package com.example.domain.model

data class WalletTransaction(
    val id: Long = 0,
    val description: String,
    val creditsDelta: Int,
    val type: String, // "REFILL", "LEAD_UNLOCK", "VERIFICATION_FEE"
    val timestamp: Long = System.currentTimeMillis()
)
