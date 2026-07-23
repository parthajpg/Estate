package com.example.domain.model

data class RentalAgreement(
    val id: Long = 0,
    val agreementNumber: String,
    val tenantName: String,
    val landlordName: String,
    val propertyAddress: String,
    val city: String,
    val monthlyRent: Double,
    val securityDeposit: Double,
    val agreementTermMonths: Int,
    val startDate: String,
    val lockInPeriodMonths: Int = 6,
    val stampDutyPaidAmount: Double = 500.0,
    val status: String = "Active",
    val timestamp: Long = System.currentTimeMillis()
)
