package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.DossierExpenseEntity
import com.example.data.local.entity.RequirementPieceEntity
import com.example.ui.components.formatFcfa
import com.example.ui.theme.CamGoldSecondary
import com.example.ui.theme.CamRedTertiary
import com.example.ui.viewmodel.DossierViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DossierDetailScreen(
    viewModel: DossierViewModel,
    dossierId: Long,
    onBack: () -> Unit,
    onFinishDossierWithTestimonial: (procedureId: String, procedureTitle: String, city: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(dossierId) {
        viewModel.selectDossier(dossierId)
    }

    val state by viewModel.selectedDossierState.collectAsStateWithLifecycle()
    val dossier = state.dossier
    val proc = state.procedure

    var showEditDatesDialog by remember { mutableStateOf(false) }
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var pieceNoteTarget by remember { mutableStateOf<RequirementPieceEntity?>(null) }

    if (dossier == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Chargement du dossier...")
        }
        return
    }

    val pieces = state.pieces
    val pieceStates = state.pieceStates
    val checkedCount = pieceStates.values.count { it.isChecked }
    val progressRatio = if (pieces.isNotEmpty()) checkedCount.toFloat() / pieces.size.toFloat() else 0f

    val totalSpent = state.expenses.sumOf { it.amountXaf }
    val officialCost = proc?.officialCostXaf ?: 0
    val costDifference = totalSpent - officialCost

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = dossier.procedureTitle,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("dossier_detail_back_button")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showDeleteConfirmDialog = true },
                        modifier = Modifier.testTag("delete_dossier_action")
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            if (dossier.status != "TERMINE") {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.updateDossierStatus("TERMINE")
                                onFinishDossierWithTestimonial(
                                    dossier.procedureId,
                                    dossier.procedureTitle,
                                    dossier.city
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("finish_dossier_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Démarche terminée ! Laisser un avis")
                        }
                    }
                }
            }
        },
        modifier = modifier.testTag("dossier_detail_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Carte de progression et statut
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Progression du dossier",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (dossier.status == "TERMINE") MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = if (dossier.status == "TERMINE") "Terminé" else "En cours",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (dossier.status == "TERMINE") MaterialTheme.colorScheme.onPrimaryContainer
                                    else MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        LinearProgressIndicator(
                            progress = { progressRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            strokeCap = StrokeCap.Round,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Text(
                            text = "$checkedCount sur ${pieces.size} pièces réunies (${(progressRatio * 100).toInt()}%)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Dates clés et Récépissé
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dates clés & Récépissé",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(
                                onClick = { showEditDatesDialog = true },
                                modifier = Modifier.size(24.dp).testTag("edit_dates_button")
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Modifier", modifier = Modifier.size(16.dp))
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("N° Récépissé", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = dossier.receiptNumber.ifEmpty { "Non renseigné" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Column {
                                Text("Date dépôt", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = dossier.depositDate.ifEmpty { "Non renseignée" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Column {
                                Text("Retrait prévu", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = dossier.targetRetireDate.ifEmpty { "Non renseignée" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        if (dossier.notes.isNotEmpty()) {
                            Text(
                                text = "Note : ${dossier.notes}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Suivi des dépenses réelles et comparateur de surcoût
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Dépenses réelles",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Comparé au tarif officiel : ${formatFcfa(officialCost)}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            OutlinedButton(
                                onClick = { showAddExpenseDialog = true },
                                modifier = Modifier.testTag("add_expense_button")
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ajouter", fontSize = 12.sp)
                            }
                        }

                        // Encadré de comparaison
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (costDifference > 0) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                            else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (costDifference > 0) Icons.Default.Warning else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (costDifference > 0) CamRedTertiary else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Total déboursé : ${formatFcfa(totalSpent)}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    if (costDifference > 0) {
                                        Text(
                                            text = "Surcoût de +${formatFcfa(costDifference)} par rapport au tarif légal",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = CamRedTertiary
                                        )
                                    } else {
                                        Text(
                                            text = "Dépenses conformes au barème officiel",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }

                        // Liste des dépenses
                        state.expenses.forEach { exp ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = exp.label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    if (exp.receiptRef.isNotEmpty()) {
                                        Text(text = "Réf : ${exp.receiptRef}", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = formatFcfa(exp.amountXaf),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (exp.isOfficialFee) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    IconButton(
                                        onClick = { viewModel.deleteExpense(exp.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer", modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Checklist des pièces personnalisée
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Pièces du dossier",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        pieces.forEach { piece ->
                            val isChecked = pieceStates[piece.id]?.isChecked ?: false
                            val userNote = pieceStates[piece.id]?.userNote ?: ""

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        viewModel.togglePieceCheck(piece.id, isChecked, userNote)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = {
                                            viewModel.togglePieceCheck(piece.id, isChecked, userNote)
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = piece.labelFr,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp
                                        )
                                        if (userNote.isNotEmpty()) {
                                            Text(
                                                text = "📝 $userNote",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = { pieceNoteTarget = piece },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Ajouter note", modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal édition dates clés
    if (showEditDatesDialog) {
        var depDate by remember { mutableStateOf(dossier.depositDate) }
        var recNum by remember { mutableStateOf(dossier.receiptNumber) }
        var retDate by remember { mutableStateOf(dossier.targetRetireDate) }
        var generalNotes by remember { mutableStateOf(dossier.notes) }

        AlertDialog(
            onDismissRequest = { showEditDatesDialog = false },
            title = { Text("Modifier les dates et le récépissé") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = recNum,
                        onValueChange = { recNum = it },
                        label = { Text("Numéro de récépissé") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("edit_receipt_input")
                    )
                    OutlinedTextField(
                        value = depDate,
                        onValueChange = { depDate = it },
                        label = { Text("Date de dépôt (ex: 28/10/2026)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("edit_deposit_input")
                    )
                    OutlinedTextField(
                        value = retDate,
                        onValueChange = { retDate = it },
                        label = { Text("Date de retrait annoncée") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("edit_retire_input")
                    )
                    OutlinedTextField(
                        value = generalNotes,
                        onValueChange = { generalNotes = it },
                        label = { Text("Notes personnelles") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateDossierKeys(depDate, recNum, retDate, generalNotes)
                        showEditDatesDialog = false
                    },
                    modifier = Modifier.testTag("save_dates_button")
                ) {
                    Text("Enregistrer")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEditDatesDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    // Modal ajouter une dépense
    if (showAddExpenseDialog) {
        var label by remember { mutableStateOf("") }
        var amountText by remember { mutableStateOf("") }
        var isOfficial by remember { mutableStateOf(false) }
        var refText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddExpenseDialog = false },
            title = { Text("Enregistrer un paiement") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = label,
                        onValueChange = { label = it },
                        label = { Text("Motif (ex: Timbre fiscal, Photocopie, Taxe)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("expense_label_input")
                    )
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text("Montant payé (FCFA)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("expense_amount_input")
                    )
                    OutlinedTextField(
                        value = refText,
                        onValueChange = { refText = it },
                        label = { Text("Numéro de quittance / reçu (optionnel)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Frais avec quittance officielle", fontSize = 12.sp)
                        Switch(checked = isOfficial, onCheckedChange = { isOfficial = it })
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = amountText.toIntOrNull() ?: 0
                        if (label.isNotEmpty() && amount > 0) {
                            viewModel.addExpense(label, amount, isOfficial, refText)
                            showAddExpenseDialog = false
                        }
                    },
                    modifier = Modifier.testTag("confirm_add_expense_button")
                ) {
                    Text("Ajouter")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddExpenseDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    // Modal note sur une pièce
    pieceNoteTarget?.let { piece ->
        var noteText by remember { mutableStateOf(pieceStates[piece.id]?.userNote ?: "") }
        AlertDialog(
            onDismissRequest = { pieceNoteTarget = null },
            title = { Text("Note pour : ${piece.labelFr}") },
            text = {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    placeholder = { Text("Ex: Copie certifiée à la mairie de Douala 1er...") },
                    modifier = Modifier.fillMaxWidth().testTag("piece_note_input")
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updatePieceNote(piece.id, noteText)
                        pieceNoteTarget = null
                    }
                ) {
                    Text("Enregistrer")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { pieceNoteTarget = null }) {
                    Text("Annuler")
                }
            }
        )
    }

    // Modal confirmation de suppression
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Supprimer ce dossier ?") },
            text = { Text("Cette action supprimera définitivement le suivi de ce dossier et ses dépenses enregistrées.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        viewModel.deleteCurrentDossier {
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("confirm_delete_dossier_button")
                ) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}
