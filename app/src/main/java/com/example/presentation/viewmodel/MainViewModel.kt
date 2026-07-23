package com.example.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.HavenDatabase
import com.example.data.repository.PropertyRepository
import com.example.domain.model.FilterState
import com.example.domain.model.Inquiry
import com.example.domain.model.PostedProperty
import com.example.domain.model.Property
import com.example.domain.model.PropertyType
import com.example.domain.model.RentalAgreement
import com.example.domain.model.SortOption
import com.example.domain.model.UserRole
import com.example.domain.model.WalletTransaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private data class SearchScreenSelectionState(
    val filter: FilterState,
    val searchMode: SearchMode,
    val selectedMapProp: Property?,
    val selectedDetailProp: Property?,
    val userRole: UserRole
)

private data class BottomSheetSelectionState(
    val isFilterSheetOpen: Boolean,
    val isContactSheetOpen: Boolean,
    val activeContactProp: Property?
)

private data class RepositoryDataState(
    val favorites: List<Property>,
    val inquiries: List<Inquiry>,
    val postedProps: List<PostedProperty>,
    val agreements: List<RentalAgreement>,
    val walletBal: Int,
    val walletTxs: List<WalletTransaction>
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = HavenDatabase.getInstance(application)
    private val repository = PropertyRepository(db.dao())

    private val _selectedRole = MutableStateFlow(UserRole.BUYER_TENANT)
    private val _filterState = MutableStateFlow(FilterState())
    private val _searchMode = MutableStateFlow(SearchMode.SPLIT)
    private val _selectedMapProperty = MutableStateFlow<Property?>(null)
    private val _selectedDetailProperty = MutableStateFlow<Property?>(null)
    private val _isFilterSheetOpen = MutableStateFlow(false)
    private val _isContactSheetOpen = MutableStateFlow(false)
    private val _activeContactProperty = MutableStateFlow<Property?>(null)
    private val _homePrice = MutableStateFlow(8_950_000.0)
    private val _downPaymentPercent = MutableStateFlow(20f)
    private val _interestRate = MutableStateFlow(6.5f)
    private val _tenureYears = MutableStateFlow(30)
    private val _inquirySuccessToast = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _filteredProperties = _filterState.flatMapLatest { filter ->
        repository.getPropertiesStream(filter)
    }

    private val _searchSelections = combine(
        _filterState,
        _searchMode,
        _selectedMapProperty,
        _selectedDetailProperty,
        _selectedRole
    ) { filter, searchMode, mapProp, detailProp, role ->
        SearchScreenSelectionState(filter, searchMode, mapProp, detailProp, role)
    }

    private val _sheetSelections = combine(
        _isFilterSheetOpen,
        _isContactSheetOpen,
        _activeContactProperty
    ) { filterOpen, contactOpen, contactProp ->
        BottomSheetSelectionState(filterOpen, contactOpen, contactProp)
    }

    private val _repoData1 = combine(
        repository.getFavoritePropertiesStream(),
        repository.getAllInquiriesStream(),
        repository.getPostedPropertiesStream()
    ) { favorites, inquiries, posted ->
        Triple(favorites, inquiries, posted)
    }

    private val _repoData2 = combine(
        repository.getRentalAgreementsStream(),
        repository.getWalletBalanceStream(),
        repository.getWalletTransactionsStream()
    ) { agreements, walletBal, walletTxs ->
        Triple(agreements, walletBal, walletTxs)
    }

    private val _repoDataState = combine(
        _repoData1,
        _repoData2
    ) { d1, d2 ->
        RepositoryDataState(
            favorites = d1.first,
            inquiries = d1.second,
            postedProps = d1.third,
            agreements = d2.first,
            walletBal = d2.second,
            walletTxs = d2.third
        )
    }

    val uiState: StateFlow<UiState> = combine(
        _filteredProperties,
        _searchSelections,
        _sheetSelections,
        _repoDataState
    ) { props, searchSel, sheetSel, repoData ->
        val mortgage = repository.calculateMortgage(
            homePrice = _homePrice.value,
            downPaymentPercent = _downPaymentPercent.value,
            interestRateAnnual = _interestRate.value,
            tenureYears = _tenureYears.value
        )

        UiState(
            isLoading = false,
            selectedUserRole = searchSel.userRole,
            properties = props,
            postedProperties = repoData.postedProps,
            savedAgreements = repoData.agreements,
            walletBalance = repoData.walletBal,
            walletTransactions = repoData.walletTxs,
            selectedPropertyForMapPreview = searchSel.selectedMapProp ?: props.firstOrNull(),
            selectedPropertyForDetail = searchSel.selectedDetailProp,
            filterState = searchSel.filter,
            isFilterBottomSheetOpen = sheetSel.isFilterSheetOpen,
            isContactAgentSheetOpen = sheetSel.isContactSheetOpen,
            activePropertyForContact = sheetSel.activeContactProp ?: searchSel.selectedDetailProp ?: props.firstOrNull(),
            searchMode = searchSel.searchMode,
            mortgageCalculation = mortgage,
            favoriteProperties = repoData.favorites,
            inquiryHistory = repoData.inquiries,
            inquirySuccessToast = _inquirySuccessToast.value
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState(isLoading = true)
    )

    fun setUserRole(role: UserRole) {
        _selectedRole.value = role
    }

    fun onCategoryTabSelected(category: String) {
        _filterState.value = _filterState.value.copy(categoryTab = category)
    }

    fun postProperty(posted: PostedProperty) {
        viewModelScope.launch {
            repository.postProperty(posted)
            _inquirySuccessToast.value = "Property '${posted.title}' published successfully!"
        }
    }

    fun deletePostedProperty(id: String) {
        viewModelScope.launch {
            repository.deletePostedProperty(id)
        }
    }

    fun saveRentalAgreement(agreement: RentalAgreement) {
        viewModelScope.launch {
            repository.saveRentalAgreement(agreement)
        }
    }

    fun addWalletCredits(description: String, credits: Int, type: String) {
        viewModelScope.launch {
            repository.addWalletCredits(description, credits, type)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _filterState.value = _filterState.value.copy(query = query)
    }

    fun onCitySelected(city: String) {
        _filterState.value = _filterState.value.copy(selectedCity = city)
    }

    fun onPriceRangeChanged(min: Float, max: Float) {
        _filterState.value = _filterState.value.copy(minPrice = min, maxPrice = max)
    }

    fun onBhkToggled(bhk: Int) {
        val current = _filterState.value.selectedBhk.toMutableSet()
        if (current.contains(bhk)) {
            current.remove(bhk)
        } else {
            current.add(bhk)
        }
        _filterState.value = _filterState.value.copy(selectedBhk = current)
    }

    fun onVerifiedOnlyToggled(verifiedOnly: Boolean) {
        _filterState.value = _filterState.value.copy(verifiedOnly = verifiedOnly)
    }

    fun onPropertyTypeSelected(type: PropertyType) {
        _filterState.value = _filterState.value.copy(selectedPropertyType = type)
    }

    fun onSortSelected(sort: SortOption) {
        _filterState.value = _filterState.value.copy(sortBy = sort)
    }

    fun resetFilters() {
        _filterState.value = FilterState()
    }

    fun setSearchMode(mode: SearchMode) {
        _searchMode.value = mode
    }

    fun selectMapProperty(property: Property?) {
        _selectedMapProperty.value = property
    }

    fun openPropertyDetail(property: Property) {
        _selectedDetailProperty.value = property
        _homePrice.value = property.priceRaw
    }

    fun closePropertyDetail() {
        _selectedDetailProperty.value = null
    }

    fun setFilterSheetOpen(isOpen: Boolean) {
        _isFilterSheetOpen.value = isOpen
    }

    fun openContactSheet(property: Property? = null) {
        _activeContactProperty.value = property ?: _selectedDetailProperty.value ?: uiState.value.properties.firstOrNull()
        _isContactSheetOpen.value = true
    }

    fun closeContactSheet() {
        _isContactSheetOpen.value = false
    }

    fun toggleFavorite(propertyId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(propertyId)
        }
    }

    fun updateMortgageHomePrice(price: Double) {
        _homePrice.value = price
    }

    fun updateMortgageDownPaymentPercent(percent: Float) {
        _downPaymentPercent.value = percent
    }

    fun updateMortgageInterestRate(rate: Float) {
        _interestRate.value = rate
    }

    fun updateMortgageTenure(years: Int) {
        _tenureYears.value = years
    }

    fun submitInquiry(
        userName: String,
        userPhone: String,
        userEmail: String,
        message: String,
        preferredTime: String
    ) {
        val prop = _activeContactProperty.value ?: return
        viewModelScope.launch {
            repository.submitInquiry(
                Inquiry(
                    propertyId = prop.id,
                    propertyTitle = prop.title,
                    userName = userName,
                    userPhone = userPhone,
                    userEmail = userEmail,
                    message = message,
                    preferredTime = preferredTime
                )
            )
            _isContactSheetOpen.value = false
            _inquirySuccessToast.value = "Inquiry sent to ${prop.agent.name}! They will reach out shortly."
        }
    }

    fun clearInquiryToast() {
        _inquirySuccessToast.value = null
    }
}
