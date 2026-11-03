package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.ProcedureEntity
import com.example.data.repository.DemarchesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CatalogUiState(
    val searchQuery: String = "",
    val selectedCity: String = "Toutes", // Douala, Yaoundé, Bafoussam, Toutes
    val selectedCategory: String = "TOUTES", // TOUTES, IDENTITE, ETAT_CIVIL, JUSTICE, ENTREPRISE, TRANSPORT
    val currentLanguage: String = "FR", // FR, EN
    val isOfflineAvailable: Boolean = true,
    val verifiedCount: Int = 8
)

class CatalogViewModel(private val repository: DemarchesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    val procedures: StateFlow<List<ProcedureEntity>> = combine(
        repository.getAllProcedures(),
        _uiState
    ) { allProcedures, state ->
        allProcedures.filter { item ->
            val matchesCategory = if (state.selectedCategory == "TOUTES") true else item.category == state.selectedCategory
            val matchesSearch = if (state.searchQuery.isBlank()) {
                true
            } else {
                val q = state.searchQuery.trim().lowercase()
                item.titleFr.lowercase().contains(q) ||
                        item.titleEn.lowercase().contains(q) ||
                        item.category.lowercase().contains(q) ||
                        item.whoIsConcernedFr.lowercase().contains(q) ||
                        // Smart Cameroonian slang / queries handling
                        (q.contains("cni") && item.id == "cni") ||
                        (q.contains("perdu") && item.id == "cni") ||
                        (q.contains("lost") && item.id == "cni") ||
                        (q.contains("sarl") && item.id == "creation_entreprise") ||
                        (q.contains("ets") && item.id == "creation_entreprise") ||
                        (q.contains("bebe") && item.id == "acte_naissance") ||
                        (q.contains("voyage") && item.id == "passeport")
            }
            matchesCategory && matchesSearch
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onCategorySelect(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onCitySelect(city: String) {
        _uiState.value = _uiState.value.copy(selectedCity = city)
    }

    fun toggleLanguage() {
        val newLang = if (_uiState.value.currentLanguage == "FR") "EN" else "FR"
        _uiState.value = _uiState.value.copy(currentLanguage = newLang)
    }
}
