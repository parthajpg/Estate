package com.example.domain.model

data class FilterState(
    val query: String = "",
    val selectedCity: String = "All Cities",
    val minPrice: Float = 0f,
    val maxPrice: Float = 15_000_000f,
    val selectedBhk: Set<Int> = emptySet(),
    val verifiedOnly: Boolean = false,
    val selectedPropertyType: PropertyType = PropertyType.ALL,
    val sortBy: SortOption = SortOption.FEATURED
)
