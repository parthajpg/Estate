package com.example.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Property
import com.example.presentation.ui.components.MapInteractiveCanvas
import com.example.presentation.ui.components.PropertyCard
import com.example.presentation.ui.components.VerifiedBadge
import com.example.presentation.viewmodel.SearchMode
import com.example.presentation.viewmodel.UiState
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.SlateDark
import com.example.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMapListScreen(
    uiState: UiState,
    onSearchQueryChanged: (String) -> Unit,
    onCitySelected: (String) -> Unit,
    onVerifiedOnlyToggled: (Boolean) -> Unit,
    onSearchModeChanged: (SearchMode) -> Unit,
    onOpenFilterSheet: () -> Unit,
    onSelectMapProperty: (Property) -> Unit,
    onOpenPropertyDetail: (Property) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onContactAgentClick: (Property) -> Unit,
    modifier: Modifier = Modifier
) {
    val popularCities = listOf("All Cities", "Malibu", "New York", "Miami", "Aspen", "London", "Dubai")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Search Bar & View Mode Controller
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, MaterialTheme.colorScheme.outline)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.filterState.query,
                    onValueChange = onSearchQueryChanged,
                    placeholder = { Text("Search location, BHK, or agent", color = Color(0xFF64748B), fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF475569))
                    },
                    trailingIcon = {
                        if (uiState.filterState.query.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChanged("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = Color(0xFF475569))
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F5F9),
                        unfocusedContainerColor = Color(0xFFF1F5F9),
                        focusedBorderColor = SlateDark,
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedTextColor = SlateDark,
                        unfocusedTextColor = SlateDark
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Filter Sheet Button
                IconButton(
                    onClick = onOpenFilterSheet,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                ) {
                    BadgedBox(
                        badge = {
                            if (uiState.filterState.verifiedOnly || uiState.filterState.selectedBhk.isNotEmpty() || uiState.filterState.selectedCity != "All Cities") {
                                Badge(containerColor = SlateDark)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filters",
                            tint = SlateDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // City Filter Pills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(popularCities) { city ->
                    val isSelected = uiState.filterState.selectedCity.equals(city, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (isSelected) SlateDark else Color.White)
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

            Spacer(modifier = Modifier.height(10.dp))

            // Search View Mode Toggle Selector (Split, Map, List)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SearchModeOption(
                    mode = SearchMode.SPLIT,
                    currentMode = uiState.searchMode,
                    icon = Icons.Default.ViewAgenda,
                    label = "Split",
                    onClick = { onSearchModeChanged(SearchMode.SPLIT) },
                    modifier = Modifier.weight(1f)
                )

                SearchModeOption(
                    mode = SearchMode.FULL_MAP,
                    currentMode = uiState.searchMode,
                    icon = Icons.Default.Map,
                    label = "Map View",
                    onClick = { onSearchModeChanged(SearchMode.FULL_MAP) },
                    modifier = Modifier.weight(1f)
                )

                SearchModeOption(
                    mode = SearchMode.FULL_LIST,
                    currentMode = uiState.searchMode,
                    icon = Icons.Default.List,
                    label = "Listings",
                    onClick = { onSearchModeChanged(SearchMode.FULL_LIST) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Active Listings Count & Quick Verified Toggle Ribbon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${uiState.properties.size} Verified Luxury Estates",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onVerifiedOnlyToggled(!uiState.filterState.verifiedOnly) }
            ) {
                VerifiedBadge(
                    text = if (uiState.filterState.verifiedOnly) "VERIFIED ONLY ON" else "ALL LISTINGS"
                )
            }
        }

        // Content Area depending on Search Mode
        when (uiState.searchMode) {
            SearchMode.SPLIT -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Map Panel (Top 45% height)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.42f)
                    ) {
                        MapInteractiveCanvas(
                            properties = uiState.properties,
                            selectedProperty = uiState.selectedPropertyForMapPreview,
                            onPropertySelected = onSelectMapProperty,
                            onOpenPropertyDetail = onOpenPropertyDetail
                        )
                    }

                    // Vertical Property Cards List (Bottom 55% height)
                    if (uiState.properties.isEmpty()) {
                        EmptyListState()
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            items(uiState.properties, key = { it.id }) { prop ->
                                PropertyCard(
                                    property = prop,
                                    onPropertyClick = onOpenPropertyDetail,
                                    onFavoriteToggle = onToggleFavorite,
                                    onContactAgentClick = onContactAgentClick
                                )
                            }
                        }
                    }
                }
            }

            SearchMode.FULL_MAP -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    MapInteractiveCanvas(
                        properties = uiState.properties,
                        selectedProperty = uiState.selectedPropertyForMapPreview,
                        onPropertySelected = onSelectMapProperty,
                        onOpenPropertyDetail = onOpenPropertyDetail
                    )
                }
            }

            SearchMode.FULL_LIST -> {
                if (uiState.properties.isEmpty()) {
                    EmptyListState()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        items(uiState.properties, key = { it.id }) { prop ->
                            PropertyCard(
                                property = prop,
                                onPropertyClick = onOpenPropertyDetail,
                                onFavoriteToggle = onToggleFavorite,
                                onContactAgentClick = onContactAgentClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchModeOption(
    mode: SearchMode,
    currentMode: SearchMode,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = mode == currentMode
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(if (isSelected) SlateDark else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.White else Color(0xFF475569),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF475569)
        )
    }
}

@Composable
private fun EmptyListState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
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
