package com.example.presentation.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import com.example.domain.model.FilterState
import com.example.domain.model.PropertyType
import com.example.domain.model.SortOption
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.SlateDark
import com.example.ui.theme.VerifiedGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    filterState: FilterState,
    onDismiss: () -> Unit,
    onCitySelected: (String) -> Unit,
    onPriceRangeChanged: (Float, Float) -> Unit,
    onBhkToggled: (Int) -> Unit,
    onVerifiedOnlyToggled: (Boolean) -> Unit,
    onPropertyTypeSelected: (PropertyType) -> Unit,
    onSortSelected: (SortOption) -> Unit,
    onResetFilters: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    val cities = listOf("All Cities", "Malibu", "New York", "Miami", "Aspen", "London", "Dubai")
    val bhkOptions = listOf(1, 2, 3, 4, 5)

    var priceRangeValue by remember(filterState) {
        mutableStateOf(filterState.minPrice..filterState.maxPrice)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Refine Luxury Search",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. 99acres Inspired Verified Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        VerifiedBadge(text = "VERIFIED LISTINGS ONLY")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Show only 100% physically inspected properties",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Switch(
                    checked = filterState.verifiedOnly,
                    onCheckedChange = onVerifiedOnlyToggled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = VerifiedGreen
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. City Filter Chips
            Text(
                text = "Target City / Region",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cities.forEach { city ->
                    val isSelected = filterState.selectedCity.equals(city, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCitySelected(city) },
                        label = { Text(text = city) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SlateDark,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Price Range Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Price Range",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                val minStr = if (priceRangeValue.start == 0f) "$0" else "$${(priceRangeValue.start / 1_000_000).toInt()}M"
                val maxStr = if (priceRangeValue.endInclusive >= 15_000_000f) "$15M+" else "$${(priceRangeValue.endInclusive / 1_000_000).toInt()}M"

                Text(
                    text = "$minStr - $maxStr",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ChampagneGold
                )
            }

            RangeSlider(
                value = priceRangeValue,
                onValueChange = { priceRangeValue = it },
                onValueChangeFinished = {
                    onPriceRangeChanged(priceRangeValue.start, priceRangeValue.endInclusive)
                },
                valueRange = 0f..15_000_000f,
                colors = SliderDefaults.colors(
                    thumbColor = SlateDark,
                    activeTrackColor = ChampagneGold
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Configuration (BHK)
            Text(
                text = "Layout Configuration (BHK)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                bhkOptions.forEach { bhk ->
                    val isSelected = filterState.selectedBhk.contains(bhk)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) SlateDark else MaterialTheme.colorScheme.background)
                            .clickable { onBhkToggled(bhk) }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = if (bhk == 5) "5+ BHK" else "$bhk BHK",
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Property Type Filter
            Text(
                text = "Property Architecture Type",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PropertyType.entries.forEach { type ->
                    val isSelected = filterState.selectedPropertyType == type
                    FilterChip(
                        selected = isSelected,
                        onClick = { onPropertyTypeSelected(type) },
                        label = { Text(text = type.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SlateDark,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 6. Sort Options
            Text(
                text = "Sort Results By",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SortOption.entries.forEach { sort ->
                    val isSelected = filterState.sortBy == sort
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSortSelected(sort) },
                        label = { Text(text = sort.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SlateDark,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Actions: Reset & Apply
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onResetFilters,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.RestartAlt, contentDescription = "Reset")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Reset All")
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1.5f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark)
                ) {
                    Text(text = "Apply Filters", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
