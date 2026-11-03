package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.ReportEntity
import com.example.data.local.entity.TestimonialEntity
import com.example.data.repository.DemarchesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CommunityUiState(
    val filterCity: String = "Toutes",
    val filterProcedureId: String = "TOUTES",
    val isSubmitDialogOpen: Boolean = false,
    val submitSuccessMsg: String? = null
)

class CommunityViewModel(private val repository: DemarchesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

    val testimonials: StateFlow<List<TestimonialEntity>> = repository.getAllTestimonials().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val reports: StateFlow<List<ReportEntity>> = repository.getAllReports().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onCityFilter(city: String) {
        _uiState.value = _uiState.value.copy(filterCity = city)
    }

    fun onProcedureFilter(procedureId: String) {
        _uiState.value = _uiState.value.copy(filterProcedureId = procedureId)
    }

    fun markHelpful(id: Long) {
        viewModelScope.launch {
            repository.incrementHelpful(id)
        }
    }

    fun submitTestimonial(
        procedureId: String,
        procedureTitle: String,
        city: String,
        serviceName: String,
        delayDays: Int,
        costPaidXaf: Int,
        rating: Int,
        comment: String,
        author: String,
        isAnonymous: Boolean
    ) {
        viewModelScope.launch {
            repository.submitTestimonial(
                procedureId = procedureId,
                procedureTitle = procedureTitle,
                city = city,
                serviceName = serviceName,
                delayDays = delayDays,
                costPaidXaf = costPaidXaf,
                rating = rating,
                comment = comment,
                authorDisplay = author,
                isAnonymous = isAnonymous
            )
            _uiState.value = _uiState.value.copy(
                submitSuccessMsg = "Votre témoignage a été publié avec anonymisation automatique des mentions nominatives."
            )
        }
    }

    fun clearSuccessMsg() {
        _uiState.value = _uiState.value.copy(submitSuccessMsg = null)
    }
}
