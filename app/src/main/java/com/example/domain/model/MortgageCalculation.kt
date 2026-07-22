package com.example.domain.model

data class MortgageCalculation(
    val homePrice: Double = 1_250_000.0,
    val downPaymentPercent: Float = 20f,
    val downPaymentAmount: Double = 250_000.0,
    val interestRateAnnual: Float = 6.5f,
    val tenureYears: Int = 30,
    val loanAmount: Double = 1_000_000.0,
    val monthlyEMI: Double = 6320.68,
    val totalInterestPaid: Double = 1_275_444.80,
    val totalPayment: Double = 2_275_444.80
)
