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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Inquiry
import com.example.domain.model.PostedProperty
import com.example.ui.theme.SlateDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDashboardScreen(
    postedProperties: List<PostedProperty>,
    inquiries: List<Inquiry>,
    onOpenPostProperty: () -> Unit,
    onDeleteProperty: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var leadSortBy by remember { mutableStateOf("LATEST") } // "LATEST" or "HIGHEST_SCORE"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("My Owner Dashboard", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("My Listings & Tenant Leads Console", fontSize = 11.sp, color = Color(0xFF64748B))
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
                        colors = ButtonDefaults.buttonColors(containerColor = SlateDark),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Post Property", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
            // Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Active Listings", fontSize = 11.sp, color = Color(0xFF64748B))
                            Text("${postedProperties.size}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = SlateDark)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Total Buyer Leads", fontSize = 11.sp, color = Color(0xFF64748B))
                            Text("${inquiries.size + 3}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = SlateDark)
                        }
                    }
                }
            }

            // Section 1: My Posted Listings
            item {
                Text(
                    text = "My Posted Properties (${postedProperties.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = SlateDark
                )
            }

            if (postedProperties.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.HomeWork, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No properties posted yet", fontWeight = FontWeight.Bold, color = SlateDark)
                            Text("Click 'Post Property' to reach thousands of buyers.", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                    }
                }
            } else {
                items(postedProperties) { prop ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(prop.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SlateDark)
                                Text("${prop.locality}, ${prop.city} • ${prop.bhk} BHK", fontSize = 12.sp, color = Color(0xFF64748B))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(prop.priceFormatted, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SlateDark)
                            }
                            IconButton(onClick = { onDeleteProperty(prop.id) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }

            // Section 2: Incoming Leads Console
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Incoming Buyer/Tenant Leads",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = SlateDark
                    )

                    Row {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (leadSortBy == "LATEST") SlateDark else Color(0xFFE2E8F0))
                                .clickable { leadSortBy = "LATEST" }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("Latest First", fontSize = 11.sp, color = if (leadSortBy == "LATEST") Color.White else SlateDark, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (leadSortBy == "HIGHEST_SCORE") SlateDark else Color(0xFFE2E8F0))
                                .clickable { leadSortBy = "HIGHEST_SCORE" }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("High Score", fontSize = 11.sp, color = if (leadSortBy == "HIGHEST_SCORE") Color.White else SlateDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Render Demo & Room DB Inquiries with qualification tags
            val sampleInquiries = listOf(
                Inquiry(
                    id = 101,
                    propertyId = "prop_1",
                    propertyTitle = "Sovereign Horizon Villa",
                    userName = "Marcus Brody",
                    userPhone = "+1 (310) 902-1144",
                    userEmail = "marcus.b@gmail.com",
                    message = "Interested in scheduling a site visit this Saturday. Pre-approved loan ready.",
                    preferredTime = "Weekend Morning",
                    status = "High Intent • Pre-Approved"
                ),
                Inquiry(
                    id = 102,
                    propertyId = "prop_2",
                    propertyTitle = "Skyline Crown Penthouse",
                    userName = "Sarah Jenkins",
                    userPhone = "+1 (212) 880-4921",
                    userEmail = "sarah.j@techcorp.com",
                    message = "Looking for 2-year lease starting Sept 1st. Please call back.",
                    preferredTime = "Evening 6-8 PM",
                    status = "Site Visit Requested"
                )
            )

            val displayInquiries = (inquiries + sampleInquiries).let { list ->
                if (leadSortBy == "HIGHEST_SCORE") list.reversed() else list
            }

            items(displayInquiries) { inq ->
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
                            Text(text = inq.userName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SlateDark)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = inq.status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Re: ${inq.propertyTitle}", fontSize = 12.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "\"${inq.message}\"", fontSize = 13.sp, color = SlateDark)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = SlateDark, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = inq.userPhone, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SlateDark)
                                    .clickable { /* action */ }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Contact Lead", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
