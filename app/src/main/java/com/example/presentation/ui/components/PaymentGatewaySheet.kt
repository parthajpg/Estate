package com.example.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.SlateDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class PaymentMethodType {
    UPI, CARD, NET_BANKING, WALLET
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentGatewaySheet(
    amount: Double,
    purpose: String,
    walletBalance: Int,
    onDismiss: () -> Unit,
    onPaymentSuccess: (txnId: String, paymentMethod: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var selectedMethod by remember { mutableStateOf(PaymentMethodType.UPI) }
    var upiVpa by remember { mutableStateOf("user@okaxis") }
    var selectedUpiApp by remember { mutableStateOf("Google Pay") }
    
    var cardNumber by remember { mutableStateOf("4532 •••• •••• 8892") }
    var cardExpiry by remember { mutableStateOf("08/28") }
    var cardCvv by remember { mutableStateOf("821") }
    var cardHolder by remember { mutableStateOf("John Doe") }

    var selectedBank by remember { mutableStateOf("HDFC Bank") }

    var isProcessing by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var generatedTxnId by remember { mutableStateOf("") }
    var paymentDate by remember { mutableStateOf("") }

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 2
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Secure Checkout",
                            tint = SlateDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Haven Secure Checkout",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = SlateDark
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "256-Bit SSL Encrypted",
                                fontSize = 11.sp,
                                color = Color(0xFF10B981),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Amount Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SlateDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = purpose,
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currencyFormatter.format(amount),
                            color = ChampagneGold,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "Instantly Confirmed",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isProcessing) {
                // Processing Animation State
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = SlateDark,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Contacting Gateway & Authorizing...",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = SlateDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Please do not close or press back",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            } else if (isSuccess) {
                // Payment Receipt State
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Payment Received!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = SlateDark
                    )
                    Text(
                        text = "Transaction completed successfully",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Transaction ID", fontSize = 12.sp, color = Color(0xFF64748B))
                                Text(generatedTxnId, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateDark)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Date & Time", fontSize = 12.sp, color = Color(0xFF64748B))
                                Text(paymentDate, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Payment Method", fontSize = 12.sp, color = Color(0xFF64748B))
                                Text(
                                    when (selectedMethod) {
                                        PaymentMethodType.UPI -> "UPI ($selectedUpiApp)"
                                        PaymentMethodType.CARD -> "Credit/Debit Card"
                                        PaymentMethodType.NET_BANKING -> "NetBanking ($selectedBank)"
                                        PaymentMethodType.WALLET -> "Haven Wallet"
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Status", fontSize = 12.sp, color = Color(0xFF64748B))
                                Text("SUCCESSFUL ✓", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            onPaymentSuccess(
                                generatedTxnId,
                                when (selectedMethod) {
                                    PaymentMethodType.UPI -> "UPI"
                                    PaymentMethodType.CARD -> "Card"
                                    PaymentMethodType.NET_BANKING -> "NetBanking"
                                    PaymentMethodType.WALLET -> "Haven Wallet"
                                }
                            )
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SlateDark)
                    ) {
                        Text("Continue & Complete Action", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            } else {
                // Payment Method Selector
                Text(
                    text = "Select Payment Method",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = SlateDark,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Options Row
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // UPI Method
                    PaymentMethodOptionCard(
                        title = "UPI / Instant Transfer",
                        subtitle = "Google Pay, PhonePe, Paytm, BHIM, VPA",
                        icon = Icons.Default.Smartphone,
                        isSelected = selectedMethod == PaymentMethodType.UPI,
                        onSelect = { selectedMethod = PaymentMethodType.UPI }
                    )

                    // Card Method
                    PaymentMethodOptionCard(
                        title = "Credit or Debit Card",
                        subtitle = "Visa, Mastercard, RuPay, American Express",
                        icon = Icons.Default.CreditCard,
                        isSelected = selectedMethod == PaymentMethodType.CARD,
                        onSelect = { selectedMethod = PaymentMethodType.CARD }
                    )

                    // NetBanking
                    PaymentMethodOptionCard(
                        title = "Net Banking",
                        subtitle = "All Major Retail & Corporate Banks",
                        icon = Icons.Default.AccountBalance,
                        isSelected = selectedMethod == PaymentMethodType.NET_BANKING,
                        onSelect = { selectedMethod = PaymentMethodType.NET_BANKING }
                    )

                    // Haven Wallet
                    PaymentMethodOptionCard(
                        title = "Haven Wallet Balance",
                        subtitle = "Available: $${walletBalance} Credits",
                        icon = Icons.Default.AccountBalanceWallet,
                        isSelected = selectedMethod == PaymentMethodType.WALLET,
                        onSelect = { selectedMethod = PaymentMethodType.WALLET }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Dynamic Method Forms
                when (selectedMethod) {
                    PaymentMethodType.UPI -> {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Fast Pay via UPI App", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Google Pay", "PhonePe", "Paytm", "BHIM").forEach { app ->
                                    val sel = selectedUpiApp == app
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (sel) SlateDark else Color(0xFFF1F5F9))
                                            .clickable { selectedUpiApp = app }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = app,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (sel) Color.White else Color(0xFF475569)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                value = upiVpa,
                                onValueChange = { upiVpa = it },
                                label = { Text("Or Enter Virtual Payment Address (VPA)") },
                                placeholder = { Text("username@upi") },
                                leadingIcon = { Icon(Icons.Default.QrCode, contentDescription = null, tint = SlateDark) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    PaymentMethodType.CARD -> {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = { cardNumber = it },
                                label = { Text("Card Number") },
                                leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null, tint = SlateDark) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = cardExpiry,
                                    onValueChange = { cardExpiry = it },
                                    label = { Text("Expiry (MM/YY)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )

                                OutlinedTextField(
                                    value = cardCvv,
                                    onValueChange = { cardCvv = it },
                                    label = { Text("CVV") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            OutlinedTextField(
                                value = cardHolder,
                                onValueChange = { cardHolder = it },
                                label = { Text("Cardholder Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    PaymentMethodType.NET_BANKING -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Select Your Retail Bank", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            listOf("HDFC Bank", "State Bank of India", "ICICI Bank", "Axis Bank", "Kotak Mahindra").forEach { bank ->
                                val sel = selectedBank == bank
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (sel) Color(0xFFF1F5F9) else Color.Transparent)
                                        .clickable { selectedBank = bank }
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = sel,
                                        onClick = { selectedBank = bank },
                                        colors = RadioButtonDefaults.colors(selectedColor = SlateDark)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(bank, fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal, fontSize = 14.sp)
                                }
                            }
                        }
                    }

                    PaymentMethodType.WALLET -> {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Haven Instant Wallet Payment",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = SlateDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Pay directly with 1-click authorization using your pre-funded credit balance.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        scope.launch {
                            isProcessing = true
                            delay(1800) // Simulate gateway call
                            isProcessing = false
                            isSuccess = true
                            generatedTxnId = "TXN_${System.currentTimeMillis().toString().takeLast(8)}"
                            paymentDate = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark)
                ) {
                    Text(
                        text = "Pay ${currencyFormatter.format(amount)} Now",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodOptionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) Color(0xFFF1F5F9) else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) SlateDark else Color(0xFFE2E8F0)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(selectedColor = SlateDark)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) SlateDark else Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else SlateDark,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SlateDark)
                Text(text = subtitle, fontSize = 11.sp, color = Color(0xFF64748B))
            }
        }
    }
}
