package com.example.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.presentation.navigation.NavRoute
import com.example.presentation.ui.components.FilterBottomSheet
import com.example.presentation.ui.components.LeadInquiryBottomSheet
import com.example.presentation.ui.components.MortgageCalculatorSection
import com.example.presentation.ui.screens.FavoritesScreen
import com.example.presentation.ui.screens.InquiryHistoryScreen
import com.example.presentation.ui.screens.PropertyDetailScreen
import com.example.presentation.ui.screens.SearchMapListScreen
import com.example.presentation.viewmodel.MainViewModel
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.SlateDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HavenAppContainer(
    mainViewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val context = LocalContext.current

    val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val contactSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Handle Toast alerts
    LaunchedEffect(uiState.inquirySuccessToast) {
        uiState.inquirySuccessToast?.let { toastText ->
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            mainViewModel.clearInquiryToast()
        }
    }

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            // Show Bottom Navigation on main tabs
            if (currentRoute != NavRoute.PropertyDetail.route) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 4.dp
                ) {
                    NavigationBarItem(
                        selected = currentRoute == NavRoute.Search.route || currentRoute == null,
                        onClick = {
                            navController.navigate(NavRoute.Search.route) {
                                popUpTo(NavRoute.Search.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Search, contentDescription = "Explore") },
                        label = { Text("Explore", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SlateDark,
                            selectedTextColor = SlateDark,
                            indicatorColor = Color(0xFFF1F5F9),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        )
                    )

                    NavigationBarItem(
                        selected = currentRoute == NavRoute.MortgageCalculator.route,
                        onClick = {
                            navController.navigate(NavRoute.MortgageCalculator.route) {
                                popUpTo(NavRoute.Search.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.AccountBalance, contentDescription = "Mortgage") },
                        label = { Text("Mortgage", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SlateDark,
                            selectedTextColor = SlateDark,
                            indicatorColor = Color(0xFFF1F5F9),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        )
                    )

                    NavigationBarItem(
                        selected = currentRoute == NavRoute.Favorites.route,
                        onClick = {
                            navController.navigate(NavRoute.Favorites.route) {
                                popUpTo(NavRoute.Search.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Saved") },
                        label = { Text("Saved", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SlateDark,
                            selectedTextColor = SlateDark,
                            indicatorColor = Color(0xFFF1F5F9),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        )
                    )

                    NavigationBarItem(
                        selected = currentRoute == NavRoute.Inquiries.route,
                        onClick = {
                            navController.navigate(NavRoute.Inquiries.route) {
                                popUpTo(NavRoute.Search.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Assignment, contentDescription = "Inquiries") },
                        label = { Text("Inquiries", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SlateDark,
                            selectedTextColor = SlateDark,
                            indicatorColor = Color(0xFFF1F5F9),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.Search.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoute.Search.route) {
                SearchMapListScreen(
                    uiState = uiState,
                    onSearchQueryChanged = mainViewModel::onSearchQueryChanged,
                    onCitySelected = mainViewModel::onCitySelected,
                    onVerifiedOnlyToggled = mainViewModel::onVerifiedOnlyToggled,
                    onSearchModeChanged = mainViewModel::setSearchMode,
                    onOpenFilterSheet = { mainViewModel.setFilterSheetOpen(true) },
                    onSelectMapProperty = mainViewModel::selectMapProperty,
                    onOpenPropertyDetail = { prop ->
                        mainViewModel.openPropertyDetail(prop)
                        navController.navigate(NavRoute.PropertyDetail.route)
                    },
                    onToggleFavorite = mainViewModel::toggleFavorite,
                    onContactAgentClick = { prop ->
                        mainViewModel.openContactSheet(prop)
                    }
                )
            }

            composable(NavRoute.PropertyDetail.route) {
                uiState.selectedPropertyForDetail?.let { prop ->
                    PropertyDetailScreen(
                        property = prop,
                        mortgageCalculation = uiState.mortgageCalculation,
                        onBackClick = {
                            mainViewModel.closePropertyDetail()
                            navController.popBackStack()
                        },
                        onFavoriteToggle = mainViewModel::toggleFavorite,
                        onContactAgentClick = { p ->
                            mainViewModel.openContactSheet(p)
                        },
                        onOpenMortgageCalculator = {
                            navController.navigate(NavRoute.MortgageCalculator.route)
                        }
                    )
                }
            }

            composable(NavRoute.MortgageCalculator.route) {
                MortgageCalculatorSection(
                    calculation = uiState.mortgageCalculation,
                    onHomePriceChanged = mainViewModel::updateMortgageHomePrice,
                    onDownPaymentPercentChanged = mainViewModel::updateMortgageDownPaymentPercent,
                    onInterestRateChanged = mainViewModel::updateMortgageInterestRate,
                    onTenureYearsChanged = mainViewModel::updateMortgageTenure,
                    modifier = Modifier.padding(16.dp)
                )
            }

            composable(NavRoute.Favorites.route) {
                FavoritesScreen(
                    favoriteProperties = uiState.favoriteProperties,
                    onPropertyClick = { prop ->
                        mainViewModel.openPropertyDetail(prop)
                        navController.navigate(NavRoute.PropertyDetail.route)
                    },
                    onFavoriteToggle = mainViewModel::toggleFavorite,
                    onContactAgentClick = { prop ->
                        mainViewModel.openContactSheet(prop)
                    }
                )
            }

            composable(NavRoute.Inquiries.route) {
                InquiryHistoryScreen(
                    inquiries = uiState.inquiryHistory
                )
            }
        }

        // Integrated Filter Bottom Sheet
        if (uiState.isFilterBottomSheetOpen) {
            FilterBottomSheet(
                filterState = uiState.filterState,
                onDismiss = { mainViewModel.setFilterSheetOpen(false) },
                onCitySelected = mainViewModel::onCitySelected,
                onPriceRangeChanged = mainViewModel::onPriceRangeChanged,
                onBhkToggled = mainViewModel::onBhkToggled,
                onVerifiedOnlyToggled = mainViewModel::onVerifiedOnlyToggled,
                onPropertyTypeSelected = mainViewModel::onPropertyTypeSelected,
                onSortSelected = mainViewModel::onSortSelected,
                onResetFilters = mainViewModel::resetFilters,
                sheetState = filterSheetState
            )
        }

        // Integrated Lead Contact Agent Bottom Sheet
        if (uiState.isContactAgentSheetOpen) {
            LeadInquiryBottomSheet(
                property = uiState.activePropertyForContact,
                onDismiss = { mainViewModel.closeContactSheet() },
                onSubmitInquiry = mainViewModel::submitInquiry,
                sheetState = contactSheetState
            )
        }
    }
}
