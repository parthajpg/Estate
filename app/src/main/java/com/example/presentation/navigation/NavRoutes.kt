package com.example.presentation.navigation

sealed class NavRoute(val route: String) {
    object Search : NavRoute("search_screen")
    object PropertyDetail : NavRoute("property_detail_screen")
    object MortgageCalculator : NavRoute("mortgage_calculator_screen")
    object Favorites : NavRoute("favorites_screen")
    object Inquiries : NavRoute("inquiries_screen")
}
