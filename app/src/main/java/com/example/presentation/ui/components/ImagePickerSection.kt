package com.example.presentation.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.SlateDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImagePickerSection(
    imageUris: List<String>,
    onImagesChanged: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(1f) }

    // Launcher for picking multiple images from Gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            scope.launch {
                isUploading = true
                uploadProgress = 0.2f
                delay(400)
                uploadProgress = 0.6f
                delay(400)
                uploadProgress = 1.0f
                delay(200)
                isUploading = false
                
                val newUriStrings = uris.map { it.toString() }
                val updatedList = (imageUris + newUriStrings).distinct()
                onImagesChanged(updatedList)
            }
        }
    }

    // Launcher for taking Camera picture preview
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            // For demo/preview, add high quality architectural photo template or photo URI
            val sampleCaptured = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=1000&q=80"
            val updatedList = (imageUris + sampleCaptured).distinct()
            onImagesChanged(updatedList)
        }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Property Photos & Media",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = SlateDark
                    )
                    Text(
                        text = "High resolution photos get 4x more buyer leads",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF1F5F9)
                ) {
                    Text(
                        text = "${imageUris.size} Photos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = SlateDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            if (isUploading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Uploading high quality assets...", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text("${(uploadProgress * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateDark)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { uploadProgress },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                        color = SlateDark,
                        trackColor = Color(0xFFE2E8F0)
                    )
                }
            }

            // Image Upload Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f).height(46.dp)
                ) {
                    Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Select from Gallery", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { cameraLauncher.launch(null) },
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateDark),
                    modifier = Modifier.weight(1f).height(46.dp)
                ) {
                    Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = null, tint = SlateDark, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Take Photo", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateDark)
                }
            }

            // Photo List Preview
            if (imageUris.isNotEmpty()) {
                Text("Uploaded Gallery Preview", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = SlateDark)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(imageUris) { index, uriString ->
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    width = if (index == 0) 2.dp else 1.dp,
                                    color = if (index == 0) ChampagneGold else Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(14.dp)
                                )
                        ) {
                            AsyncImage(
                                model = uriString,
                                contentDescription = "Property Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth().height(110.dp)
                            )

                            // Cover Badge on index 0
                            if (index == 0) {
                                Surface(
                                    color = SlateDark,
                                    shape = RoundedCornerShape(bottomEnd = 10.dp),
                                    modifier = Modifier.align(Alignment.TopStart)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = ChampagneGold,
                                            modifier = Modifier.size(10.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text("Cover", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // Delete Button
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .clickable {
                                        val updated = imageUris.toMutableList().apply { removeAt(index) }
                                        onImagesChanged(updated)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                // Empty state quick presets
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No photos added yet",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Quick Sample Preset Gallery:",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=1000&q=80",
                            "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=1000&q=80",
                            "https://images.unsplash.com/photo-1600566753376-12c8ab7fb75b?w=1000&q=80"
                        ).forEachIndexed { i, sampleUrl ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE2E8F0),
                                modifier = Modifier
                                    .clickable {
                                        val updated = (imageUris + sampleUrl).distinct()
                                        onImagesChanged(updated)
                                    }
                            ) {
                                Text(
                                    text = "+ Add Preset #${i + 1}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SlateDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
