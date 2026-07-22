package com.example.domain.model

enum class PropertyType(val label: String) {
    ALL("All Types"),
    VILLA("Villa"),
    PENTHOUSE("Penthouse"),
    APARTMENT("Apartment"),
    TOWNHOUSE("Townhouse"),
    WATERFRONT("Waterfront")
}

enum class SortOption(val label: String) {
    FEATURED("Featured"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
    AREA_DESC("Size: Largest First")
}

data class Agent(
    val name: String,
    val role: String,
    val agency: String,
    val phone: String,
    val whatsappNumber: String,
    val email: String,
    val rating: Float,
    val verifiedTransactions: Int,
    val avatarUrl: String
)

data class Property(
    val id: String,
    val title: String,
    val tagline: String,
    val priceFormatted: String,
    val priceRaw: Double,
    val location: String,
    val city: String,
    val neighborhood: String,
    val bhk: Int,
    val bathrooms: Int,
    val areaSqFt: Int,
    val isVerified: Boolean = true,
    val isFeatured: Boolean = false,
    val type: PropertyType,
    val images: List<String>,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val amenities: List<String>,
    val agent: Agent,
    val isFavorite: Boolean = false
)
