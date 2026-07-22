package com.example.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.Property
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.SlateDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.VerifiedGreen

@Composable
fun MapInteractiveCanvas(
    properties: List<Property>,
    selectedProperty: Property?,
    onPropertySelected: (Property) -> Unit,
    onOpenPropertyDetail: (Property) -> Unit,
    modifier: Modifier = Modifier
) {
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }

    Box(modifier = modifier.fillMaxSize()) {
        // High Quality Interactive Vector Map Canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE2E8F0))
                .pointerInput(properties) {
                    detectTapGestures { tapOffset ->
                        val mapWidth = size.width.toFloat()
                        val mapHeight = size.height.toFloat()

                        var closest: Property? = null
                        var minDistance = Float.MAX_VALUE

                        properties.forEachIndexed { index, property ->
                            // Map coordinates projection
                            val posX = ((index % 3) * 0.3f + 0.2f) * mapWidth
                            val posY = ((index / 3) * 0.35f + 0.25f) * mapHeight

                            val dx = tapOffset.x - posX
                            val dy = tapOffset.y - posY
                            val dist = dx * dx + dy * dy
                            if (dist < minDistance && dist < 120 * 120) {
                                minDistance = dist
                                closest = property
                            }
                        }

                        closest?.let { onPropertySelected(it) }
                    }
                }
        ) {
            val w = size.width
            val h = size.height

            // 1. Water Body / Ocean Coastline
            val waterPath = Path().apply {
                moveTo(0f, h * 0.75f)
                cubicTo(w * 0.3f, h * 0.65f, w * 0.6f, h * 0.85f, w, h * 0.70f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(path = waterPath, color = Color(0xFFBAE6FD))

            // 2. Parks & Greenery
            drawRoundRect(
                color = Color(0xFFDCFCE7),
                topLeft = Offset(w * 0.1f, h * 0.1f),
                size = Size(w * 0.25f, h * 0.2f),
                cornerRadius = CornerRadius(24f, 24f)
            )

            // 3. Grid Streets Network
            val roadColor = Color.White
            val mainRoadColor = Color(0xFFCBD5E1)

            for (i in 1..8) {
                val y = h * (i / 9f)
                drawLine(
                    color = roadColor,
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 12f * zoomLevel
                )
            }
            for (i in 1..6) {
                val x = w * (i / 7f)
                drawLine(
                    color = roadColor,
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 12f * zoomLevel
                )
            }

            // Diagonal Highway
            drawLine(
                color = mainRoadColor,
                start = Offset(0f, 0f),
                end = Offset(w, h * 0.7f),
                strokeWidth = 20f * zoomLevel
            )

            // 4. Property Pins with Price Tag Badges
            properties.forEachIndexed { index, property ->
                val posX = ((index % 3) * 0.3f + 0.2f) * w
                val posY = ((index / 3) * 0.35f + 0.25f) * h
                val isSelected = selectedProperty?.id == property.id

                // Pin base glow
                if (isSelected) {
                    drawCircle(
                        color = Color(0xFFD97706).copy(alpha = 0.3f),
                        radius = 48f * zoomLevel,
                        center = Offset(posX, posY)
                    )
                }

                // Pin Pinhead Circle
                drawCircle(
                    color = if (isSelected) Color(0xFFD97706) else Color(0xFF0F172A),
                    radius = 18f * zoomLevel,
                    center = Offset(posX, posY)
                )

                drawCircle(
                    color = Color.White,
                    radius = 8f * zoomLevel,
                    center = Offset(posX, posY)
                )
            }
        }

        // Overlay Price Tag Labels over Pins
        properties.forEachIndexed { index, property ->
            val isSelected = selectedProperty?.id == property.id
            // Calculate overlay positioning matching canvas ratio
            val alignBiasX = ((index % 3) * 0.3f + 0.2f) * 2f - 1f
            val alignBiasY = ((index / 3) * 0.35f + 0.25f) * 2f - 1f

            Box(
                modifier = Modifier
                    .align(BiasAlignment(alignBiasX, alignBiasY - 0.08f))
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) SlateDark else Color.White)
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) ChampagneGold else Color(0xFFCBD5E1),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onPropertySelected(property) }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = property.priceFormatted,
                    color = if (isSelected) ChampagneGold else SlateDark,
                    fontSize = if (isSelected) 13.sp else 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Map Controls Floating Column
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            FloatingMapButton(
                icon = Icons.Default.Add,
                contentDescription = "Zoom In"
            ) { zoomLevel = (zoomLevel + 0.2f).coerceAtMost(2.0f) }

            Spacer(modifier = Modifier.height(8.dp))

            FloatingMapButton(
                icon = Icons.Default.Remove,
                contentDescription = "Zoom Out"
            ) { zoomLevel = (zoomLevel - 0.2f).coerceAtLeast(0.6f) }

            Spacer(modifier = Modifier.height(8.dp))

            FloatingMapButton(
                icon = Icons.Default.LocationSearching,
                contentDescription = "Recenter"
            ) { zoomLevel = 1.0f }
        }

        // Bottom Selected Property Quick Preview Card
        AnimatedVisibility(
            visible = selectedProperty != null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            selectedProperty?.let { prop ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenPropertyDetail(prop) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = prop.images.firstOrNull(),
                            contentDescription = prop.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(14.dp))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = prop.priceFormatted,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (prop.isVerified) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    VerifiedBadge(text = "VERIFIED")
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = prop.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "${prop.bhk} BHK • ${prop.bathrooms} Baths • ${prop.city}",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }

                        IconButton(
                            onClick = { onOpenPropertyDetail(prop) },
                            modifier = Modifier
                                .background(SlateDark, CircleShape)
                                .size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "View Details",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingMapButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, Color(0xFFCBD5E1), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = SlateDark,
            modifier = Modifier.size(20.dp)
        )
    }
}

private class BiasAlignment(
    private val horizontalBias: Float,
    private val verticalBias: Float
) : Alignment {
    override fun align(
        size: androidx.compose.ui.unit.IntSize,
        space: androidx.compose.ui.unit.IntSize,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection
    ): androidx.compose.ui.unit.IntOffset {
        val x = (space.width - size.width) / 2f * (1 + horizontalBias)
        val y = (space.height - size.height) / 2f * (1 + verticalBias)
        return androidx.compose.ui.unit.IntOffset(x.toInt(), y.toInt())
    }
}
