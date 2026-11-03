package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.LocationEntity
import com.example.data.repository.DemarchesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class LocationUiState(
    val selectedCity: String = "Toutes",
    val selectedCategory: String = "TOUTES",
    val selectedLocation: LocationEntity? = null
)

class LocationViewModel(private val repository: DemarchesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    val locations: StateFlow<List<LocationEntity>> = combine(
        repository.getAllLocations(),
        _uiState
    ) { all, state ->
        all.filter { loc ->
            val matchesCity = if (state.selectedCity == "Toutes") true else loc.city == state.selectedCity
            val matchesCategory = if (state.selectedCategory == "TOUTES") true else loc.category == state.selectedCategory
            matchesCity && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onCitySelect(city: String) {
        _uiState.value = _uiState.value.copy(selectedCity = city)
    }

    fun onCategorySelect(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun selectLocation(location: LocationEntity?) {
        _uiState.value = _uiState.value.copy(selectedLocation = location)
    }
}
