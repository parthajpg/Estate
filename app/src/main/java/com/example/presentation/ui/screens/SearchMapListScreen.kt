package com.example.presentation.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import com.example.presentation.ui.components.YohesGoldAccent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.Property
import com.example.domain.model.UserRole
import com.example.presentation.ui.components.PropertyCard
import com.example.presentation.ui.components.VerifiedBadge
import com.example.presentation.ui.components.YohesHeaderLogo
import com.example.presentation.ui.components.YohesBlueDark
import com.example.presentation.ui.components.YohesGoldAccent
import com.example.presentation.viewmodel.UiState
import com.example.ui.theme.SlateDark
import com.example.ui.theme.TextMuted
import kotlinx.coroutines.delay

data class HeroSlide(
    val imageUrl: String,
    val headline: String,
    val subtitle: String,
    val locationPrice: String,
    val propertyId: String
)

val heroSlides = listOf(
    HeroSlide(
        imageUrl = "https://images.unsplash.com/photo-1613977257363-707ba9348227?auto=format&fit=crop&w=1200&q=80",
        headline = "The World's\nLuxury Marketplace",
        subtitle = "ONE SEARCH • 830,000+ LISTINGS • 21,000+ TRUSTED SELLERS • 140 COUNTRIES",
        locationPrice = "APARTMENT IN MILOVIĆI, TIVAT MUNICIPALITY, MONTENEGRO • $4,200,000",
        propertyId = "p1"
    ),
    HeroSlide(
        imageUrl = "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=1200&q=80",
        headline = "Beverly Hills\nCoastal Haven",
        subtitle = "EXCLUSIVE PRIVATE ESTATES • SPECTACULAR OCEAN PANORAMAS",
        locationPrice = "BEVERLY HILLS MODERN OCEAN VILLA, CALIFORNIA • $18,500,000",
        propertyId = "p2"
    ),
    HeroSlide(
        imageUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=1200&q=80",
        headline = "Manhattan Sky\nPenthouses",
        subtitle = "ULTRA-LUXURY RESIDENCES • 360 SKYLINE VIEWS",
        locationPrice = "MANHATTAN SKYLINE PENTHOUSE, NEW YORK • $24,000,000",
        propertyId = "p3"
    ),
    HeroSlide(
        imageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=1200&q=80",
        headline = "Alpine Ski Chalet\nEstates",
        subtitle = "SKI-IN SKI-OUT SANCTUARIES • ASPEN COLORADO",
        locationPrice = "ALPINE SKI CHALET, ASPEN COLORADO • $12,800,000",
        propertyId = "p4"
    ),
    HeroSlide(
        imageUrl = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?auto=format&fit=crop&w=1200&q=80",
        headline = "Palm Jumeirah\nWaterfront Palace",
        subtitle = "PRIVATE BEACHFRONT VILLAS • DUBAI UAE",
        locationPrice = "PALM JUMEIRAH WATERFRONT PALACE, DUBAI • $32,000,000",
        propertyId = "p5"
    )
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchMapListScreen(
    uiState: UiState,
    onSearchQueryChanged: (String) -> Unit,
    onCategoryTabSelected: (String) -> Unit = {},
    onCitySelected: (String) -> Unit,
    onVerifiedOnlyToggled: (Boolean) -> Unit,
    onOpenFilterSheet: () -> Unit,
    onOpenPropertyDetail: (Property) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onContactAgentClick: (Property) -> Unit,
    onOpenRoleSelector: () -> Unit = {},
    onOpenPostProperty: () -> Unit = {},
    onOpenDealerConsole: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val popularCities = listOf("All Cities", "Malibu", "New York", "Miami", "Aspen", "London", "Dubai")
    val categoryTabs = listOf("Real Estate", "Villas", "Penthouses", "Estates", "Rentals", "Journal")

    var currentSlideIndex by remember { mutableIntStateOf(0) }

    // Auto slideshow effect - advances every 4.5 seconds
    LaunchedEffect(currentSlideIndex) {
        delay(4500)
        currentSlideIndex = (currentSlideIndex + 1) % heroSlides.size
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // JamesEdition Style Top Header Bar
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(YohesBlueDark)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onOpenFilterSheet) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                        YohesHeaderLogo(textColor = Color.White)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                                .clickable { onOpenPostProperty() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Sell",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                                .clickable { onOpenDealerConsole() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Agents",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Log In / Mode Switch Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(YohesGoldAccent)
                                .clickable { onOpenRoleSelector() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = YohesBlueDark,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = when (uiState.selectedUserRole) {
                                        UserRole.BUYER_TENANT -> "Log In"
                                        UserRole.INDIVIDUAL_OWNER -> "Owner"
                                        UserRole.BROKER_DEALER -> "Dealer"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = YohesBlueDark
                                )
                            }
                        }
                    }
                }

                // JamesEdition Category Row
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0A1E3F))
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(categoryTabs) { category ->
                        val isSelected = uiState.filterState.categoryTab.equals(category, ignoreCase = true)
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) YohesGoldAccent else Color.White.copy(alpha = 0.8f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onCategoryTabSelected(category) }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Hero Image Auto-Slideshow Section (JamesEdition Style)
        item {
            val slide = heroSlides[currentSlideIndex]

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .background(Color.Black)
            ) {
                // Animated Image Background
                AnimatedContent(
                    targetState = slide,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(700)) togetherWith fadeOut(animationSpec = tween(700))
                    },
                    label = "heroSlide"
                ) { targetSlide ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = targetSlide.imageUrl,
                            contentDescription = targetSlide.headline,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    val matchedProp = uiState.properties.find { it.id == targetSlide.propertyId }
                                        ?: uiState.properties.firstOrNull()
                                    matchedProp?.let { onOpenPropertyDetail(it) }
                                }
                        )

                        // Dark Gradient Overlay for Crisp White Text Readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.35f),
                                            Color.Black.copy(alpha = 0.15f),
                                            Color.Black.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )
                    }
                }

                // Overlay Headline & Tagline
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = slide.headline,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 34.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = slide.subtitle,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f),
                        letterSpacing = 1.sp
                    )
                }

                // Left & Right Navigation Arrows
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp, bottom = 40.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            currentSlideIndex = if (currentSlideIndex > 0) currentSlideIndex - 1 else heroSlides.size - 1
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            currentSlideIndex = (currentSlideIndex + 1) % heroSlides.size
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next",
                            tint = Color.White
                        )
                    }
                }

                // Bottom Right Location & Price Caption
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black.copy(alpha = 0.65f))
                        .clickable {
                            val matchedProp = uiState.properties.find { it.id == slide.propertyId }
                                ?: uiState.properties.firstOrNull()
                            matchedProp?.let { onOpenPropertyDetail(it) }
                        }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = slide.locationPrice,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = YohesGoldAccent,
                        letterSpacing = 0.5.sp
                    )
                }

                // Bottom Timer Indicators (Lines showing active slide)
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    heroSlides.indices.forEach { index ->
                        val isActive = index == currentSlideIndex
                        val alpha = if (isActive) 1f else 0.35f
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.White.copy(alpha = alpha))
                                .clickable { currentSlideIndex = index }
                        )
                    }
                }
            }
        }

        // Search Controls Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Custom Groomed Luxury Search Bar
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF1F5F9))
                            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = YohesBlueDark,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (uiState.filterState.query.isEmpty()) {
                                Text(
                                    text = "Search location, villa, city...",
                                    color = Color(0xFF64748B),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1
                                )
                            }
                            BasicTextField(
                                value = uiState.filterState.query,
                                onValueChange = onSearchQueryChanged,
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = SlateDark,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                cursorBrush = SolidColor(YohesBlueDark),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (uiState.filterState.query.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchQueryChanged("") },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = Color(0xFF475569),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Filter Button
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(14.dp))
                            .clickable { onOpenFilterSheet() },
                        contentAlignment = Alignment.Center
                    ) {
                        BadgedBox(
                            badge = {
                                if (uiState.filterState.verifiedOnly || uiState.filterState.selectedBhk.isNotEmpty() || uiState.filterState.selectedCity != "All Cities") {
                                    Badge(containerColor = YohesGoldAccent)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filters",
                                tint = YohesBlueDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // City Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(popularCities) { city ->
                        val isSelected = uiState.filterState.selectedCity.equals(city, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(if (isSelected) YohesBlueDark else Color.White)
                                .border(
                                    width = if (isSelected) 0.dp else 1.dp,
                                    color = Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .clickable { onCitySelected(city) }
                                .padding(horizontal = 16.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = city,
                                color = if (isSelected) Color.White else Color(0xFF475569),
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Active Listings Count & Quick Verified Toggle Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${uiState.properties.size} Exclusive Yohes Listings",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = YohesBlueDark
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onVerifiedOnlyToggled(!uiState.filterState.verifiedOnly) }
                ) {
                    VerifiedBadge(
                        text = if (uiState.filterState.verifiedOnly) "VERIFIED ONLY" else "ALL LISTINGS"
                    )
                }
            }
        }

        // Property Cards List
        if (uiState.properties.isEmpty()) {
            item {
                EmptyListState()
            }
        } else {
            items(uiState.properties, key = { it.id }) { prop ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    PropertyCard(
                        property = prop,
                        onPropertyClick = onOpenPropertyDetail,
                        onFavoriteToggle = onToggleFavorite,
                        onContactAgentClick = onContactAgentClick
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EmptyListState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Empty",
                tint = TextMuted,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No Luxury Estates Match Criteria",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Try clearing filters or searching for another region.",
                fontSize = 13.sp,
                color = TextMuted
            )
        }
    }
}
