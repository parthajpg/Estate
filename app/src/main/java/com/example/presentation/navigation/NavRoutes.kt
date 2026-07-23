package com.example.presentation.navigation

sealed class NavRoute(val route: String) {
    object Search : NavRoute("search_screen")
    object PropertyDetail : NavRoute("property_detail_screen")
    object MortgageCalculator : NavRoute("mortgage_calculator_screen")
    object Favorites : NavRoute("favorites_screen")
    object Inquiries : NavRoute("inquiries_screen")
    object PostProperty : NavRoute("post_property_screen")
    object OwnerDashboard : NavRoute("owner_dashboard_screen")
    object DealerConsole : NavRoute("dealer_console_screen")
    object RentalAgreement : NavRoute("rental_agreement_screen")
}
