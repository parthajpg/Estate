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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.PostedProperty
import com.example.presentation.ui.components.ImagePickerSection
import com.example.ui.theme.SlateDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostPropertyScreen(
    userType: String = "Individual Owner", // "Individual Owner" or "Broker / Dealer"
    onPropertySubmitted: (PostedProperty) -> Unit,
    onBackClick: () -> Unit
) {
    var step by remember { mutableStateOf(1) } // 1 to 4 steps

    var category by remember { mutableStateOf("Sell") } // "Sell" or "Rent"
    var title by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("New York") }
    var locality by remember { mutableStateOf("Manhattan") }
    var towerHouseNo by remember { mutableStateOf("Tower 4, Unit 12B") }

    var bhk by remember { mutableStateOf("3") }
    var carpetArea by remember { mutableStateOf("2100") }
    var bathrooms by remember { mutableStateOf("3") }
    var furnishing by remember { mutableStateOf("Fully Furnished") }

    var priceOrRent by remember { mutableStateOf("1850000") }
    var ownerName by remember { mutableStateOf("Siva") }
    var ownerPhone by remember { mutableStateOf("+1 (212) 555-0199") }
    var ownerEmail = "siva@havenestate.com"
    var uploadedPhotos by remember {
        mutableStateOf(
            listOf(
                "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=1000&q=80",
                "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=1000&q=80"
            )
        )
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = SlateDark,
        unfocusedBorderColor = Color(0xFFCBD5E1),
        focusedLabelColor = SlateDark,
        unfocusedLabelColor = Color(0xFF64748B),
        cursorColor = SlateDark,
        focusedTextColor = SlateDark,
        unfocusedTextColor = SlateDark,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color(0xFFF8FAFC)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Post Property for $userType", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Step $step of 4 • Free Instant Listing", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
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
            item {
                // Step Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1, 2, 3, 4).forEach { s ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (s <= step) SlateDark else Color(0xFFE2E8F0))
                        )
                    }
                }
            }

            when (step) {
                1 -> {
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Text("Step 1: Listing Type & Address", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SlateDark)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("Sell", "Rent").forEach { opt ->
                                        val sel = category == opt
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (sel) SlateDark else Color(0xFFF1F5F9))
                                                .clickable { category = opt }
                                                .padding(vertical = 12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = opt,
                                                fontWeight = FontWeight.Bold,
                                                color = if (sel) Color.White else Color(0xFF64748B)
                                            )
                                        }
                                    }
                                }

                                OutlinedTextField(
                                    value = title,
                                    onValueChange = { title = it },
                                    label = { Text("Property Title") },
                                    placeholder = { Text("e.g. Modern Sunset Park Penthouse") },
                                    leadingIcon = { Icon(Icons.Default.HomeWork, contentDescription = null, tint = SlateDark) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = textFieldColors
                                )

                                OutlinedTextField(
                                    value = city,
                                    onValueChange = { city = it },
                                    label = { Text("City") },
                                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = SlateDark) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = textFieldColors
                                )

                                OutlinedTextField(
                                    value = locality,
                                    onValueChange = { locality = it },
                                    label = { Text("Locality / Neighborhood") },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = textFieldColors
                                )

                                OutlinedTextField(
                                    value = towerHouseNo,
                                    onValueChange = { towerHouseNo = it },
                                    label = { Text("Tower / Building / House No") },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = textFieldColors
                                )

                                Button(
                                    onClick = { if (title.isNotBlank()) step = 2 },
                                    modifier = Modifier.fillMaxWidth().height(50.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark, contentColor = Color.White)
                                ) {
                                    Text("Next: Property Configuration", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                2 -> {
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Text("Step 2: Configuration & Size", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SlateDark)

                                OutlinedTextField(
                                    value = bhk,
                                    onValueChange = { bhk = it },
                                    label = { Text("Bedrooms (BHK)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = textFieldColors
                                )

                                OutlinedTextField(
                                    value = carpetArea,
                                    onValueChange = { carpetArea = it },
                                    label = { Text("Carpet Area (Sq.Ft.)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = textFieldColors
                                )

                                OutlinedTextField(
                                    value = bathrooms,
                                    onValueChange = { bathrooms = it },
                                    label = { Text("Bathrooms") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = textFieldColors
                                )

                                Text("Furnishing Status", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("Unfurnished", "Semi-Furnished", "Fully Furnished").forEach { f ->
                                        val sel = furnishing == f
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (sel) SlateDark else Color(0xFFF1F5F9))
                                                .clickable { furnishing = f }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = f,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (sel) Color.White else Color(0xFF64748B)
                                            )
                                        }
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Button(
                                        onClick = { step = 1 },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E8F0), contentColor = SlateDark),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.weight(1f).height(50.dp)
                                    ) {
                                        Text("Back", color = SlateDark, fontWeight = FontWeight.Bold)
                                    }

                                    Button(
                                        onClick = { step = 3 },
                                        colors = ButtonDefaults.buttonColors(containerColor = SlateDark, contentColor = Color.White),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.weight(1f).height(50.dp)
                                    ) {
                                        Text("Next: Pricing", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Text("Step 3: Expected Pricing & Owner Info", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SlateDark)

                                OutlinedTextField(
                                    value = priceOrRent,
                                    onValueChange = { priceOrRent = it },
                                    label = { Text(if (category == "Rent") "Expected Monthly Rent ($)" else "Expected Selling Price ($)") },
                                    leadingIcon = { Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = SlateDark) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = textFieldColors
                                )

                                OutlinedTextField(
                                    value = ownerName,
                                    onValueChange = { ownerName = it },
                                    label = { Text("Contact Name") },
                                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SlateDark) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = textFieldColors
                                )

                                OutlinedTextField(
                                    value = ownerPhone,
                                    onValueChange = { ownerPhone = it },
                                    label = { Text("Contact Phone") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = textFieldColors
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                ImagePickerSection(
                                    imageUris = uploadedPhotos,
                                    onImagesChanged = { uploadedPhotos = it }
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Button(
                                        onClick = { step = 2 },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E8F0), contentColor = SlateDark),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.weight(1f).height(50.dp)
                                    ) {
                                        Text("Back", color = SlateDark, fontWeight = FontWeight.Bold)
                                    }

                                    Button(
                                        onClick = { step = 4 },
                                        colors = ButtonDefaults.buttonColors(containerColor = SlateDark, contentColor = Color.White),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.weight(1f).height(50.dp)
                                    ) {
                                        Text("Next: Review", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                4 -> {
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Step 4: Review & Publish", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SlateDark)
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
                                        .padding(16.dp)
                                ) {
                                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SlateDark)
                                    Text(text = "$locality, $city • $towerHouseNo", fontSize = 12.sp, color = Color(0xFF64748B))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "$bhk BHK • $carpetArea Sq.Ft. • $furnishing", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    val numericVal = priceOrRent.toDoubleOrNull() ?: 1000000.0
                                    val formattedPrice = if (category == "Rent") "$${numericVal.toInt()}/mo" else "$${String.format("%,.0f", numericVal)}"
                                    Text(text = "Listed Price: $formattedPrice", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SlateDark)
                                }

                                Button(
                                    onClick = {
                                        val numericVal = priceOrRent.toDoubleOrNull() ?: 1000000.0
                                        val formattedPrice = if (category == "Rent") "$${numericVal.toInt()}/mo" else "$${String.format("%,.0f", numericVal)}"
                                        val posted = PostedProperty(
                                            id = "post_${System.currentTimeMillis()}",
                                            title = title,
                                            tagline = "$bhk BHK $category Listing in $locality",
                                            category = category,
                                            city = city,
                                            locality = locality,
                                            towerHouseNo = towerHouseNo,
                                            bhk = bhk.toIntOrNull() ?: 3,
                                            carpetAreaSqFt = carpetArea.toIntOrNull() ?: 1800,
                                            bathrooms = bathrooms.toIntOrNull() ?: 2,
                                            furnishing = furnishing,
                                            priceOrRent = numericVal,
                                            priceFormatted = formattedPrice,
                                            description = "Newly listed property posted via Haven $userType Console.",
                                            imageUrl = uploadedPhotos.firstOrNull() ?: "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=1000&q=80",
                                            ownerType = userType,
                                            ownerName = ownerName,
                                            ownerPhone = ownerPhone,
                                            ownerEmail = ownerEmail,
                                            isVerified = true
                                        )
                                        onPropertySubmitted(posted)
                                    },
                                    modifier = Modifier.fillMaxWidth().height(50.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark, contentColor = Color.White)
                                ) {
                                    Text("Publish Property Now", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
