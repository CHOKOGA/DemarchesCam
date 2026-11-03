package com.example.ui.screens

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.PreseedData
import com.example.data.local.entity.ProcedureEntity
import com.example.data.repository.DemarchesRepository
import com.example.ui.components.formatFcfa
import com.example.ui.viewmodel.DossierViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDossierScreen(
    dossierViewModel: DossierViewModel,
    repository: DemarchesRepository,
    onBack: () -> Unit,
    onDossierCreated: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val familyProfiles by repository.getAllFamilyProfiles().collectAsStateWithLifecycle(initialValue = emptyList())

    var step by remember { mutableIntStateOf(1) } // 1: Choix Démarche, 2: Variante, Ville & Bénéficiaire, 3: Confirmation
    var selectedProcedure by remember { mutableStateOf<ProcedureEntity?>(PreseedData.procedures.firstOrNull()) }
    var selectedVariantTitle by remember { mutableStateOf("Général") }
    var selectedCity by remember { mutableStateOf("Yaoundé") }
    var selectedBeneficiary by remember { mutableStateOf("Moi") }

    val variants = remember(selectedProcedure) {
        PreseedData.variants.filter { it.procedureId == selectedProcedure?.id }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nouveau Dossier (Étape $step/3)",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (step > 1) step-- else onBack()
                        },
                        modifier = Modifier.testTag("new_dossier_back_button")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        modifier = modifier.testTag("new_dossier_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (step) {
                1 -> {
                    Text(
                        text = "1. Choisissez la démarche à entreprendre :",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(PreseedData.procedures, key = { it.id }) { proc ->
                            val isSelected = selectedProcedure?.id == proc.id
                            Card(
                                onClick = {
                                    selectedProcedure = proc
                                    selectedVariantTitle = PreseedData.variants.find { it.procedureId == proc.id }?.titleFr ?: "Standard"
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth().testTag("select_proc_${proc.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = proc.titleFr,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "Coût officiel : ${formatFcfa(proc.officialCostXaf)} • ${proc.officialDelayDays} jours",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Sélectionné",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { step = 2 },
                        enabled = selectedProcedure != null,
                        modifier = Modifier.fillMaxWidth().testTag("step_1_next_button")
                    ) {
                        Text("Continuer vers la configuration")
                    }
                }

                2 -> {
                    Text(
                        text = "2. Bénéficiaire, situation & ville :",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Choix du bénéficiaire (Mes Proches)
                    Text("Pour qui effectuez-vous cette démarche ?", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val allBeneficiaries = if (familyProfiles.isEmpty()) listOf("Moi") else familyProfiles.map { it.name }
                        allBeneficiaries.forEach { name ->
                            FilterChip(
                                selected = selectedBeneficiary == name,
                                onClick = { selectedBeneficiary = name },
                                label = { Text(name) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Ville où vous déposez le dossier :", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Yaoundé", "Douala", "Bafoussam").forEach { city ->
                            FilterChip(
                                selected = selectedCity == city,
                                onClick = { selectedCity = city },
                                label = { Text(city) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Situation / Variante :", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

                    if (variants.isEmpty()) {
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                            Text("Procédure standard unique.", modifier = Modifier.padding(14.dp), fontSize = 13.sp)
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            variants.forEach { v ->
                                val isSelected = selectedVariantTitle == v.titleFr
                                Card(
                                    onClick = { selectedVariantTitle = v.titleFr },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                                        else MaterialTheme.colorScheme.surface
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = v.titleFr, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(text = v.descriptionFr, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(onClick = { step = 1 }, modifier = Modifier.weight(1f)) {
                            Text("Retour")
                        }
                        Button(onClick = { step = 3 }, modifier = Modifier.weight(1f).testTag("step_2_next_button")) {
                            Text("Récapitulatif")
                        }
                    }
                }

                3 -> {
                    Text(
                        text = "3. Récapitulatif du dossier à générer :",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = selectedProcedure?.titleFr ?: "",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text("Bénéficiaire : $selectedBeneficiary", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text("Option : $selectedVariantTitle", fontSize = 13.sp)
                            Text("Ville : $selectedCity", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text("Coût officiel légal : ${formatFcfa(selectedProcedure?.officialCostXaf ?: 0)}", fontSize = 13.sp)
                            Text("Délai annoncé : ${selectedProcedure?.officialDelayDays} jours ouvrables", fontSize = 13.sp)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "💡 Dès création, vous accéderez à votre checklist interactive des pièces, pourrez noter votre numéro de récépissé lors du dépôt et comparer vos dépenses au tarif légal.",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            val proc = selectedProcedure ?: return@Button
                            dossierViewModel.createNewDossier(
                                procedureId = proc.id,
                                procedureTitle = proc.titleFr,
                                variantTitle = "$selectedVariantTitle (Pour : $selectedBeneficiary)",
                                city = selectedCity
                            ) { newId ->
                                onDossierCreated(newId)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("create_dossier_final_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Générer mon dossier de suivi")
                    }
                }
            }
        }
    }
}
