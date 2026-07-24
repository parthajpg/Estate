package com.example.presentation.viewmodel

import com.example.domain.model.FilterState
import com.example.domain.model.Inquiry
import com.example.domain.model.MortgageCalculation
import com.example.domain.model.PostedProperty
import com.example.domain.model.Property
import com.example.domain.model.RentalAgreement
import com.example.domain.model.UserRole
import com.example.domain.model.WalletTransaction

enum class SearchMode(val title: String) {
    SPLIT("Split Map & List"),
    FULL_MAP("Interactive Map"),
    FULL_LIST("List View")
}

data class UiState(
    val isLoading: Boolean = false,
    val selectedUserRole: UserRole = UserRole.BUYER_TENANT,
    val properties: List<Property> = emptyList(),
    val postedProperties: List<PostedProperty> = emptyList(),
    val savedAgreements: List<RentalAgreement> = emptyList(),
    val walletBalance: Int = 250,
    val walletTransactions: List<WalletTransaction> = emptyList(),
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
    val inquirySuccessToast: String? = null,
    val isPhoneVerified: Boolean = false,
    val verifiedPhoneNumber: String = ""
)
