package com.example.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.HavenDatabase
import com.example.data.repository.PropertyRepository
import com.example.domain.model.FilterState
import com.example.domain.model.Inquiry
import com.example.domain.model.Property
import com.example.domain.model.PropertyType
import com.example.domain.model.SortOption
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
    val selectedDetailProp: Property?
)

private data class BottomSheetSelectionState(
    val isFilterSheetOpen: Boolean,
    val isContactSheetOpen: Boolean,
    val activeContactProp: Property?
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = HavenDatabase.getInstance(application)
    private val repository = PropertyRepository(db.dao())

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
        _selectedDetailProperty
    ) { filter, searchMode, mapProp, detailProp ->
        SearchScreenSelectionState(filter, searchMode, mapProp, detailProp)
    }

    private val _sheetSelections = combine(
        _isFilterSheetOpen,
        _isContactSheetOpen,
        _activeContactProperty
    ) { filterOpen, contactOpen, contactProp ->
        BottomSheetSelectionState(filterOpen, contactOpen, contactProp)
    }

    val uiState: StateFlow<UiState> = combine(
        _filteredProperties,
        _searchSelections,
        _sheetSelections,
        repository.getFavoritePropertiesStream(),
        repository.getAllInquiriesStream()
    ) { props, searchSel, sheetSel, favorites, inquiries ->
        val mortgage = repository.calculateMortgage(
            homePrice = _homePrice.value,
            downPaymentPercent = _downPaymentPercent.value,
            interestRateAnnual = _interestRate.value,
            tenureYears = _tenureYears.value
        )

        UiState(
            properties = props,
            selectedPropertyForMapPreview = searchSel.selectedMapProp ?: props.firstOrNull(),
            selectedPropertyForDetail = searchSel.selectedDetailProp,
            filterState = searchSel.filter,
            isFilterBottomSheetOpen = sheetSel.isFilterSheetOpen,
            isContactAgentSheetOpen = sheetSel.isContactSheetOpen,
            activePropertyForContact = sheetSel.activeContactProp ?: searchSel.selectedDetailProp ?: props.firstOrNull(),
            searchMode = searchSel.searchMode,
            mortgageCalculation = mortgage,
            favoriteProperties = favorites,
            inquiryHistory = inquiries,
            inquirySuccessToast = _inquirySuccessToast.value
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState(isLoading = true)
    )

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
