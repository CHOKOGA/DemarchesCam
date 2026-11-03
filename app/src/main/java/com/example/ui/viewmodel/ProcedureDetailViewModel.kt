package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.LocationEntity
import com.example.data.local.entity.ProcedureEntity
import com.example.data.local.entity.ProcedureStepEntity
import com.example.data.local.entity.ProcedureVariantEntity
import com.example.data.local.entity.RequirementPieceEntity
import com.example.data.local.entity.TestimonialEntity
import com.example.data.local.entity.UserDossierEntity
import com.example.data.repository.DemarchesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProcedureDetailUiState(
    val procedure: ProcedureEntity? = null,
    val variants: List<ProcedureVariantEntity> = emptyList(),
    val selectedVariantId: String = "",
    val allPieces: List<RequirementPieceEntity> = emptyList(),
    val steps: List<ProcedureStepEntity> = emptyList(),
    val testimonials: List<TestimonialEntity> = emptyList(),
    val locations: List<LocationEntity> = emptyList(),
    val checkedPieceIds: Set<String> = emptySet(),
    val reportSuccessMessage: String? = null,
    val dossierCreatedId: Long? = null
)

class ProcedureDetailViewModel(
    private val repository: DemarchesRepository,
    private val procedureId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProcedureDetailUiState())
    val uiState: StateFlow<ProcedureDetailUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getProcedureById(procedureId).collect { proc ->
                _uiState.value = _uiState.value.copy(procedure = proc)
            }
        }
        viewModelScope.launch {
            repository.getVariantsForProcedure(procedureId).collect { variants ->
                val firstId = variants.firstOrNull()?.id ?: ""
                _uiState.value = _uiState.value.copy(
                    variants = variants,
                    selectedVariantId = if (_uiState.value.selectedVariantId.isEmpty()) firstId else _uiState.value.selectedVariantId
                )
            }
        }
        viewModelScope.launch {
            repository.getPiecesForProcedure(procedureId).collect { pieces ->
                _uiState.value = _uiState.value.copy(allPieces = pieces)
            }
        }
        viewModelScope.launch {
            repository.getStepsForProcedure(procedureId).collect { steps ->
                _uiState.value = _uiState.value.copy(steps = steps)
            }
        }
        viewModelScope.launch {
            repository.getTestimonialsForProcedure(procedureId).collect { list ->
                _uiState.value = _uiState.value.copy(testimonials = list)
            }
        }
        viewModelScope.launch {
            repository.getAllLocations().collect { locs ->
                _uiState.value = _uiState.value.copy(locations = locs)
            }
        }
    }

    fun selectVariant(variantId: String) {
        _uiState.value = _uiState.value.copy(selectedVariantId = variantId)
    }

    fun togglePieceCheck(pieceId: String) {
        val current = _uiState.value.checkedPieceIds.toMutableSet()
        if (current.contains(pieceId)) {
            current.remove(pieceId)
        } else {
            current.add(pieceId)
        }
        _uiState.value = _uiState.value.copy(checkedPieceIds = current)
    }

    fun createDossierFromThis(city: String, onComplete: (Long) -> Unit) {
        val proc = _uiState.value.procedure ?: return
        val currentVar = _uiState.value.variants.find { it.id == _uiState.value.selectedVariantId }
        val varTitle = currentVar?.titleFr ?: "Général"

        viewModelScope.launch {
            val newDossier = UserDossierEntity(
                procedureId = proc.id,
                procedureTitle = proc.titleFr,
                variantTitle = varTitle,
                city = city,
                status = "EN_COURS"
            )
            val newId = repository.createDossier(newDossier)

            // Save checked pieces if any
            _uiState.value.checkedPieceIds.forEach { pieceId ->
                repository.setPieceState(newId, pieceId, isChecked = true)
            }

            _uiState.value = _uiState.value.copy(dossierCreatedId = newId)
            onComplete(newId)
        }
    }

    fun submitReport(field: String, costReported: Int, comment: String) {
        val proc = _uiState.value.procedure ?: return
        viewModelScope.launch {
            repository.submitReport(
                procedureId = proc.id,
                procedureTitle = proc.titleFr,
                fieldReported = field,
                userCostReportedXaf = costReported,
                userComment = comment
            )
            _uiState.value = _uiState.value.copy(
                reportSuccessMessage = "Signalement enregistré. Merci pour votre contribution citoyenne !"
            )
        }
    }

    fun clearReportMessage() {
        _uiState.value = _uiState.value.copy(reportSuccessMessage = null)
    }
}
