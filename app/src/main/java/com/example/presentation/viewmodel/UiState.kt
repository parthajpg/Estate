package com.example.presentation.viewmodel

import com.example.domain.model.FilterState
import com.example.domain.model.Inquiry
import com.example.domain.model.MortgageCalculation
import com.example.domain.model.Property

enum class SearchMode(val title: String) {
    SPLIT("Split Map & List"),
    FULL_MAP("Interactive Map"),
    FULL_LIST("List View")
}

data class UiState(
    val isLoading: Boolean = false,
    val properties: List<Property> = emptyList(),
    val selectedPropertyForMapPreview: Property? = null,
    val selectedPropertyForDetail: Property? = null,
    val filterState: FilterState = FilterState(),
    val isFilterBottomSheetOpen: Boolean = false,
    val isContactAgentSheetOpen: Boolean = false,
    val activePropertyForContact: Property? = null,
    val searchMode: SearchMode = SearchMode.SPLIT,
    val mortgageCalculation: MortgageCalculation = MortgageCalculation(),
    val favoriteProperties: List<Property> = emptyList(),
    val inquiryHistory: List<Inquiry> = emptyList(),
    val isSubmittingInquiry: Boolean = false,
    val inquirySuccessToast: String? = null
)
