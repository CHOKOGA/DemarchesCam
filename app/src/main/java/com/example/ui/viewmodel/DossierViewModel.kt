package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.DossierExpenseEntity
import com.example.data.local.entity.DossierPieceStateEntity
import com.example.data.local.entity.ProcedureEntity
import com.example.data.local.entity.RequirementPieceEntity
import com.example.data.local.entity.UserDossierEntity
import com.example.data.repository.DemarchesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DossierDetailUiState(
    val dossier: UserDossierEntity? = null,
    val procedure: ProcedureEntity? = null,
    val pieces: List<RequirementPieceEntity> = emptyList(),
    val pieceStates: Map<String, DossierPieceStateEntity> = emptyMap(),
    val expenses: List<DossierExpenseEntity> = emptyList(),
    val isFinishedModalOpen: Boolean = false
)

class DossierViewModel(private val repository: DemarchesRepository) : ViewModel() {

    val allDossiers: StateFlow<List<UserDossierEntity>> = repository.getAllDossiers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedDossierState = MutableStateFlow(DossierDetailUiState())
    val selectedDossierState: StateFlow<DossierDetailUiState> = _selectedDossierState.asStateFlow()

    fun selectDossier(dossierId: Long) {
        viewModelScope.launch {
            repository.getDossierById(dossierId).collect { dos ->
                if (dos != null) {
                    _selectedDossierState.value = _selectedDossierState.value.copy(dossier = dos)
                    loadDossierComplements(dos)
                }
            }
        }
    }

    private fun loadDossierComplements(dossier: UserDossierEntity) {
        viewModelScope.launch {
            repository.getProcedureById(dossier.procedureId).collect { proc ->
                _selectedDossierState.value = _selectedDossierState.value.copy(procedure = proc)
            }
        }
        viewModelScope.launch {
            repository.getPiecesForProcedure(dossier.procedureId).collect { pieces ->
                _selectedDossierState.value = _selectedDossierState.value.copy(pieces = pieces)
            }
        }
        viewModelScope.launch {
            repository.getPieceStatesForDossier(dossier.id).collect { states ->
                val stateMap = states.associateBy { it.pieceId }
                _selectedDossierState.value = _selectedDossierState.value.copy(pieceStates = stateMap)
            }
        }
        viewModelScope.launch {
            repository.getExpensesForDossier(dossier.id).collect { expList ->
                _selectedDossierState.value = _selectedDossierState.value.copy(expenses = expList)
            }
        }
    }

    fun togglePieceCheck(pieceId: String, currentChecked: Boolean, note: String = "") {
        val dossierId = _selectedDossierState.value.dossier?.id ?: return
        viewModelScope.launch {
            repository.setPieceState(dossierId, pieceId, !currentChecked, note)
        }
    }

    fun updatePieceNote(pieceId: String, note: String) {
        val dossierId = _selectedDossierState.value.dossier?.id ?: return
        val isChecked = _selectedDossierState.value.pieceStates[pieceId]?.isChecked ?: false
        viewModelScope.launch {
            repository.setPieceState(dossierId, pieceId, isChecked, note)
        }
    }

    fun updateDossierKeys(depositDate: String, receiptNumber: String, targetRetireDate: String, notes: String) {
        val current = _selectedDossierState.value.dossier ?: return
        val updated = current.copy(
            depositDate = depositDate,
            receiptNumber = receiptNumber,
            targetRetireDate = targetRetireDate,
            notes = notes
        )
        viewModelScope.launch {
            repository.updateDossier(updated)
        }
    }

    fun updateDossierStatus(newStatus: String) {
        val current = _selectedDossierState.value.dossier ?: return
        val updated = current.copy(status = newStatus)
        viewModelScope.launch {
            repository.updateDossier(updated)
        }
    }

    fun addExpense(label: String, amountXaf: Int, isOfficial: Boolean, receiptRef: String) {
        val dossierId = _selectedDossierState.value.dossier?.id ?: return
        viewModelScope.launch {
            repository.addExpense(
                DossierExpenseEntity(
                    dossierId = dossierId,
                    label = label,
                    amountXaf = amountXaf,
                    isOfficialFee = isOfficial,
                    receiptRef = receiptRef,
                    dateString = "Aujourd'hui"
                )
            )
        }
    }

    fun deleteExpense(expenseId: Long) {
        viewModelScope.launch {
            repository.deleteExpense(expenseId)
        }
    }

    fun deleteCurrentDossier(onDeleted: () -> Unit) {
        val id = _selectedDossierState.value.dossier?.id ?: return
        viewModelScope.launch {
            repository.deleteDossier(id)
            onDeleted()
        }
    }

    fun createNewDossier(
        procedureId: String,
        procedureTitle: String,
        variantTitle: String,
        city: String,
        onCreated: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val id = repository.createDossier(
                UserDossierEntity(
                    procedureId = procedureId,
                    procedureTitle = procedureTitle,
                    variantTitle = variantTitle,
                    city = city,
                    status = "EN_COURS"
                )
            )
            onCreated(id)
        }
    }
}
