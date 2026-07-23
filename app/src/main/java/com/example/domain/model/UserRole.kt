package com.example.domain.model

enum class UserRole(
    val title: String,
    val subtitle: String,
    val badge: String
) {
    BUYER_TENANT(
        title = "Buyer / Tenant",
        subtitle = "Explore properties, view price trends & rental agreements",
        badge = "Buyer"
    ),
    INDIVIDUAL_OWNER(
        title = "Individual Owner",
        subtitle = "Post property, track leads & manage listings",
        badge = "Owner"
    ),
    BROKER_DEALER(
        title = "Broker / Dealer",
        subtitle = "Dealer console, bulk listing, credits & lead scoring",
        badge = "Dealer Pro"
    )
}
