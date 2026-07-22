package com.example.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.MortgageCalculation
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.SlateDark
import com.example.ui.theme.TextMuted
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MortgageCalculatorSection(
    calculation: MortgageCalculation,
    onHomePriceChanged: (Double) -> Unit,
    onDownPaymentPercentChanged: (Float) -> Unit,
    onInterestRateChanged: (Float) -> Unit,
    onTenureYearsChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormat = rememberCurrencyFormat()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Square Yards Mortgage Calculator",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Estimate monthly EMI, down payment, and total loan interest",
                fontSize = 12.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Monthly EMI Result Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SlateDark)
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ESTIMATED MONTHLY PAYMENT (EMI)",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currencyFormat.format(calculation.monthlyEMI) + " / mo",
                        color = ChampagneGold,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Visual Principal vs Interest Bar Chart
                    val loanShare = if (calculation.totalPayment > 0) {
                        (calculation.loanAmount / calculation.totalPayment).toFloat()
                    } else 0.5f

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(loanShare.coerceAtLeast(0.05f))
                                .height(10.dp)
                                .background(ChampagneGold)
                        )
                        Box(
                            modifier = Modifier
                                .weight((1f - loanShare).coerceAtLeast(0.05f))
                                .height(10.dp)
                                .background(Color(0xFF38BDF8))
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(ChampagneGold)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Principal: ${currencyFormat.format(calculation.loanAmount)}",
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color(0xFF38BDF8))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Interest: ${currencyFormat.format(calculation.totalInterestPaid)}",
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Home Price Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Home Price", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    text = currencyFormat.format(calculation.homePrice),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Slider(
                value = calculation.homePrice.toFloat(),
                onValueChange = { onHomePriceChanged(it.toDouble()) },
                valueRange = 500_000f..20_000_000f,
                colors = SliderDefaults.colors(
                    thumbColor = SlateDark,
                    activeTrackColor = ChampagneGold
                )
            )

            // 2. Down Payment Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Down Payment", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    text = "${calculation.downPaymentPercent.toInt()}% (${currencyFormat.format(calculation.downPaymentAmount)})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Slider(
                value = calculation.downPaymentPercent,
                onValueChange = onDownPaymentPercentChanged,
                valueRange = 5f..50f,
                colors = SliderDefaults.colors(
                    thumbColor = SlateDark,
                    activeTrackColor = ChampagneGold
                )
            )

            // 3. Interest Rate Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Annual Interest Rate", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    text = "${String.format(Locale.US, "%.1f", calculation.interestRateAnnual)}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Slider(
                value = calculation.interestRateAnnual,
                onValueChange = onInterestRateChanged,
                valueRange = 3.0f..12.0f,
                colors = SliderDefaults.colors(
                    thumbColor = SlateDark,
                    activeTrackColor = ChampagneGold
                )
            )

            // 4. Tenure Chips
            Text(text = "Loan Tenure (Years)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(10, 15, 20, 25, 30).forEach { years ->
                    val isSelected = calculation.tenureYears == years
                    FilterChip(
                        selected = isSelected,
                        onClick = { onTenureYearsChanged(years) },
                        label = { Text(text = "$years Yrs") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SlateDark,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun rememberCurrencyFormat(): NumberFormat {
    return NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }
}
