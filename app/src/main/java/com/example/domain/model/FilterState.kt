package com.example.domain.model

data class FilterState(
    val query: String = "",
    val categoryTab: String = "Buy", // "Buy", "Rent", "Commercial", "New Projects"
    val constructionStatus: String = "All", // "All", "Ready to Move", "Under Construction"
    val selectedCity: String = "All Cities",
    val minPrice: Float = 0f,
    val maxPrice: Float = 15_000_000f,
    val selectedBhk: Set<Int> = emptySet(),
    val verifiedOnly: Boolean = false,
    val selectedPropertyType: PropertyType = PropertyType.ALL,
    val sortBy: SortOption = SortOption.FEATURED
)
