package com.example.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.WalletTransaction
import com.example.ui.theme.SlateDark
import kotlinx.coroutines.launch

data class ScoredLead(
    val id: String,
    val name: String,
    val phoneMasked: String,
    val phoneUnmasked: String,
    val propertyInterest: String,
    val budget: String,
    val timeline: String,
    val stars: Int, // 1 to 3 stars
    var isUnlocked: Boolean = false,
    val costCredits: Int = 20
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealerConsoleScreen(
    walletBalance: Int,
    walletTransactions: List<WalletTransaction>,
    onAddCredits: suspend (description: String, credits: Int, type: String) -> Unit,
    onOpenPostProperty: () -> Unit,
    onBackClick: () -> Unit
) {
    var verificationStatus by remember { mutableStateOf("Verified Pro Broker") }
    var isDocumentSubmitted by remember { mutableStateOf(false) }

    var leadsList by remember {
        mutableStateOf(
            listOf(
                ScoredLead(
                    id = "lead_1",
                    name = "Siva",
                    phoneMasked = "+1 (310) 8XX-XX19",
                    phoneUnmasked = "+1 (310) 892-4100",
                    propertyInterest = "Skyline Penthouse & Malibu Villa",
                    budget = "$5,000,000+",
                    timeline = "Immediate (15 days)",
                    stars = 3,
                    isUnlocked = false
                ),
                ScoredLead(
                    id = "lead_2",
                    name = "ParthaSarathy",
                    phoneMasked = "+1 (212) 4XX-XX82",
                    phoneUnmasked = "+1 (212) 431-8822",
                    propertyInterest = "TriBeCa Loft / 4 BHK",
                    budget = "$3,200,000",
                    timeline = "Within 1 Month",
                    stars = 3,
                    isUnlocked = false
                ),
                ScoredLead(
                    id = "lead_3",
                    name = "Siva Partha",
                    phoneMasked = "+1 (305) 9XX-XX04",
                    phoneUnmasked = "+1 (305) 912-3004",
                    propertyInterest = "Miami Waterfront Villa",
                    budget = "$8,000,000",
                    timeline = "Inquiry Only",
                    stars = 2,
                    isUnlocked = true
                )
            )
        )
    }

    val scope = rememberCoroutineScope()
    var messageBanner by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Broker & Dealer Console", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Monetization, Bulk Listing & Scored Lead Funnel", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = onOpenPostProperty,
                        colors = ButtonDefaults.buttonColors(containerColor = SlateDark, contentColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Bulk Add", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Executive KPI Cards
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Lead Wallet Balance", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, tint = Color(0xFFF59E0B))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("$walletBalance Credits", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        onAddCredits("Top-up +250 Credits Package", 250, "REFILL")
                                        messageBanner = "Successfully added +250 Credits to your Lead Wallet!"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("+ Refill $100", color = SlateDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text("Verified Badge", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                                    Text(verificationStatus, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text("Conversion Rate", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                                    Text("18.4% High Intent", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            if (messageBanner.isNotEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(messageBanner, color = Color(0xFF065F46), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Section 1: Verification Queue
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Dealer Verification Queue", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SlateDark)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Upload license or title deed to retain 'Verified Dealer' status & get 3x higher buyer responses.",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (!isDocumentSubmitted) {
                            Button(
                                onClick = {
                                    isDocumentSubmitted = true
                                    verificationStatus = "Verification Pending Review"
                                    messageBanner = "Title deed & REA license documents submitted for audit!"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SlateDark, contentColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Upload Relationship / Ownership Document", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Documents uploaded. Verified badge renewed.", fontSize = 12.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Section 2: Algorithmically Scored Buyer Leads Funnel
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Scored Hot Leads (Paid Funnel)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = SlateDark
                    )
                    Text(
                        text = "20 Credits per Unlock",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD97706)
                    )
                }
            }

            items(leadsList) { lead ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(lead.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SlateDark)
                            Row {
                                repeat(lead.stars) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(14.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Target: ${lead.propertyInterest}", fontSize = 12.sp, color = Color(0xFF64748B))
                        Text("Budget: ${lead.budget} • Timeline: ${lead.timeline}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = SlateDark)

                        Spacer(modifier = Modifier.height(12.dp))

                        if (lead.isUnlocked) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFECFDF5), RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Call, contentDescription = null, tint = Color(0xFF047857), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(lead.phoneUnmasked, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF047857))
                                }
                                Text("Unlocked Lead", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(lead.phoneMasked, fontSize = 13.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)

                                Button(
                                    onClick = {
                                        if (walletBalance >= lead.costCredits) {
                                            scope.launch {
                                                onAddCredits("Unlocked Lead: ${lead.name}", -lead.costCredits, "LEAD_UNLOCK")
                                                leadsList = leadsList.map {
                                                    if (it.id == lead.id) it.copy(isUnlocked = true) else it
                                                }
                                                messageBanner = "Lead contact unlocked for ${lead.name}!"
                                            }
                                        } else {
                                            messageBanner = "Insufficient credits. Please refill your wallet!"
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark, contentColor = Color.White),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Unlock (-20 Credits)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Section 3: Credit Usage History
            item {
                Text(
                    text = "Credit Usage History",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = SlateDark
                )
            }

            if (walletTransactions.isEmpty()) {
                item {
                    Text("Initial welcome credits: 250 free credits active.", fontSize = 12.sp, color = Color(0xFF64748B))
                }
            } else {
                items(walletTransactions) { tx ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(tx.description, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SlateDark)
                            Text(tx.type, fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                        Text(
                            text = if (tx.creditsDelta > 0) "+${tx.creditsDelta}" else "${tx.creditsDelta}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (tx.creditsDelta > 0) Color(0xFF10B981) else Color(0xFFEF4444)
                        )
                    }
                }
            }
        }
    }
}
