package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val creditsDelta: Int, // positive for topup, negative for unlock
    val type: String, // "REFILL", "LEAD_UNLOCK", "VERIFICATION_FEE"
    val timestamp: Long = System.currentTimeMillis()
)
