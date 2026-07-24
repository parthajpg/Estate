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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneOtpSheet(
    title: String = "Mobile Phone Verification",
    subtitle: String = "Verify your phone number for secure access & instant landlord contact",
    onDismiss: () -> Unit,
    onVerifiedSuccess: (phoneNumber: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var step by remember { mutableStateOf(1) } // 1: Enter Phone, 2: OTP Verification, 3: Success
    var countryCode by remember { mutableStateOf("+1") }
    var phoneNumber by remember { mutableStateOf("5550192834") }
    
    var digit1 by remember { mutableStateOf("8") }
    var digit2 by remember { mutableStateOf("4") }
    var digit3 by remember { mutableStateOf("9") }
    var digit4 by remember { mutableStateOf("2") }
    var digit5 by remember { mutableStateOf("0") }
    var digit6 by remember { mutableStateOf("1") }

    var resendTimer by remember { mutableStateOf(30) }
    var showSmsNotification by remember { mutableStateOf(false) }
    var generatedOtpCode by remember { mutableStateOf("849201") }
    var errorMessage by remember { mutableStateOf("") }

    // Countdown Timer for Resend OTP
    LaunchedEffect(step, resendTimer) {
        if (step == 2 && resendTimer > 0) {
            delay(1000)
            resendTimer -= 1
        }
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
            // Top Bar
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
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = SlateDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = SlateDark
                        )
                        Text(
                            text = "Instant SMS OTP Authentication",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated Push Notification Banner when OTP is sent
            AnimatedVisibility(visible = showSmsNotification) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SlateDark,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(ChampagneGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Message,
                                contentDescription = null,
                                tint = SlateDark,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "SMS Notification Received",
                                color = ChampagneGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Yohes Haven OTP code is $generatedOtpCode. Valid for 10 minutes.",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            if (step == 1) {
                // Step 1: Mobile Phone Number Input
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Country Code Selector
                    Row(
                        modifier = Modifier
                            .width(90.dp)
                            .height(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF1F5F9))
                            .clickable {
                                countryCode = if (countryCode == "+1") "+91" else if (countryCode == "+91") "+44" else "+1"
                            }
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = countryCode,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = SlateDark
                        )
                    }

                    // Phone input
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it; errorMessage = "" },
                        label = { Text("Phone Number") },
                        placeholder = { Text("10 digit mobile number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = SlateDark) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SlateDark,
                            unfocusedBorderColor = Color(0xFFCBD5E1)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (phoneNumber.length < 7) {
                            errorMessage = "Please enter a valid phone number"
                        } else {
                            step = 2
                            showSmsNotification = true
                            resendTimer = 30
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark, contentColor = Color.White)
                ) {
                    Text("Send SMS Verification Code", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            } else if (step == 2) {
                // Step 2: 6-Digit OTP Box View
                Text(
                    text = "We have sent a 6-digit verification code to $countryCode $phoneNumber",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 6 Pin Digit Input Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    listOf(
                        digit1 to { v: String -> digit1 = v },
                        digit2 to { v: String -> digit2 = v },
                        digit3 to { v: String -> digit3 = v },
                        digit4 to { v: String -> digit4 = v },
                        digit5 to { v: String -> digit5 = v },
                        digit6 to { v: String -> digit6 = v }
                    ).forEach { (valStr, setVal) ->
                        OutlinedTextField(
                            value = valStr,
                            onValueChange = { input ->
                                if (input.length <= 1) setVal(input)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = SlateDark
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SlateDark,
                                unfocusedBorderColor = Color(0xFFCBD5E1)
                            ),
                            modifier = Modifier
                                .width(46.dp)
                                .height(56.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Fast Auto-Fill Tap Button
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier
                        .clickable {
                            digit1 = "8"
                            digit2 = "4"
                            digit3 = "9"
                            digit4 = "2"
                            digit5 = "0"
                            digit6 = "1"
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Auto-fill SMS Code: 849201",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateDark
                        )
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Resend Timer Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (resendTimer > 0) {
                        Text(
                            text = "Resend SMS in ${resendTimer}s",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    } else {
                        Text(
                            text = "Resend SMS Code",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateDark,
                            modifier = Modifier.clickable {
                                resendTimer = 30
                                showSmsNotification = true
                            }
                        )
                    }

                    Text(
                        text = "Change Number",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2563EB),
                        modifier = Modifier.clickable { step = 1 }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val entered = "$digit1$digit2$digit3$digit4$digit5$digit6"
                        if (entered == generatedOtpCode || entered.length == 6) {
                            step = 3
                            onVerifiedSuccess("$countryCode $phoneNumber")
                        } else {
                            errorMessage = "Invalid code. Tap auto-fill code 849201."
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark, contentColor = Color.White)
                ) {
                    Text("Verify Phone Number", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            } else {
                // Step 3: Success Screen
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Phone Verified Successfully!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = SlateDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Verified Number: $countryCode $phoneNumber",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark, contentColor = Color.White)
                ) {
                    Text("Done", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
