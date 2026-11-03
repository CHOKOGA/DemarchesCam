package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.ProcedureStepEntity
import com.example.data.local.entity.RequirementPieceEntity
import com.example.data.repository.DemarchesRepository
import com.example.ui.components.PieceCheckItem
import com.example.ui.components.StepTimelineItem
import com.example.ui.components.TestimonialCardItem
import com.example.ui.components.TrustDisclaimerBanner
import com.example.ui.components.VerifiedBadge
import com.example.ui.components.formatFcfa
import com.example.ui.theme.CamGoldSecondary
import com.example.ui.theme.CamRedTertiary
import com.example.ui.viewmodel.ProcedureDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcedureDetailScreen(
    viewModel: ProcedureDetailViewModel,
    repository: DemarchesRepository,
    onBack: () -> Unit,
    onDossierCreated: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val proc = uiState.procedure
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showReportDialog by remember { mutableStateOf(false) }
    var showStartDossierDialog by remember { mutableStateOf(false) }
    var selectedCityForDossier by remember { mutableStateOf("Yaoundé") }
    var isSavedOffline by remember { mutableStateOf(false) }

    if (proc == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Chargement de la fiche démarche...")
        }
        return
    }

    val applicablePieces = uiState.allPieces.filter {
        it.variantId.isEmpty() || it.variantId == uiState.selectedVariantId
    }

    fun shareOnWhatsApp() {
        val shareText = buildString {
            append("🇨🇲 *DémarchesCam — Guide Citoyen Officiel*\n")
            append("📄 *${proc.titleFr}*\n\n")
            append("💰 *Coût officiel légal :* ${formatFcfa(proc.officialCostXaf)}\n")
            if (proc.estimatedExtraCostXaf > 0) {
                append("💵 *Frais annexes estimés :* ~${formatFcfa(proc.estimatedExtraCostXaf)}\n")
            }
            append("⏱️ *Délai moyen constaté :* ~${proc.observedDelayDays} jours\n")
            append("🏛️ *Source officielle :* ${proc.officialSourceName}\n\n")
            append("📋 *Pièces obligatoires à fournir :*\n")
            applicablePieces.forEach { piece ->
                val type = if (piece.isOriginalRequired) "[Original]" else if (piece.certifiedCopyRequired) "[Copie certifiée]" else "[Photocopie]"
                append("• ${piece.labelFr} $type\n")
            }
            append("\n⚠️ *Rappel : Ne payez aucun frais sans quittance officielle.*")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Partager la fiche de démarche")
        context.startActivity(shareIntent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = proc.titleFr,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_to_catalog_button")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { shareOnWhatsApp() },
                        modifier = Modifier.testTag("share_whatsapp_action")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Partager sur WhatsApp",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = { showReportDialog = true },
                        modifier = Modifier.testTag("report_issue_action")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReportProblem,
                            contentDescription = "Signaler un écart",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { shareOnWhatsApp() },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("detail_share_whatsapp_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("WhatsApp", fontSize = 12.sp)
                    }
                    Button(
                        onClick = { showStartDossierDialog = true },
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("start_dossier_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Suivre mon dossier")
                    }
                }
            }
        },
        modifier = modifier.testTag("procedure_detail_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TrustDisclaimerBanner()
            }

            // Résumé en haut
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
                            VerifiedBadge(verifiedDate = proc.verifiedDate)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = proc.category,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            text = proc.titleFr,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Grille métrique
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Coût officiel", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = formatFcfa(proc.officialCostXaf),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text("+ ~${formatFcfa(proc.estimatedExtraCostXaf)} annexes", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                            }
                            HorizontalDivider(modifier = Modifier.height(36.dp).width(1.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Délai officiel", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = "${proc.officialDelayDays} jours",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text("Constaté : ~${proc.observedDelayDays}j", fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary)
                            }
                        }

                        // Bouton Téléchargement hors-ligne
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSavedOffline) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    isSavedOffline = !isSavedOffline
                                    coroutineScope.launch {
                                        repository.toggleOfflineSave(proc.id, !isSavedOffline)
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = if (isSavedOffline) Icons.Default.CloudDone else Icons.Default.CloudDownload,
                                    contentDescription = null,
                                    tint = if (isSavedOffline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isSavedOffline) "Fiche enregistrée pour usage hors-ligne" else "Télécharger la fiche pour usage hors-ligne",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSavedOffline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Source officielle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInBrowser,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Source officielle : ${proc.officialSourceName}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Qui est concerné
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Qui est concerné ?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = proc.whoIsConcernedFr,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Variantes
            if (uiState.variants.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Votre situation (Variante) :",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.variants.forEach { variant ->
                                val isSelected = variant.id == uiState.selectedVariantId
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.selectVariant(variant.id) },
                                    label = { Text(variant.titleFr) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    modifier = Modifier.testTag("variant_chip_${variant.id}")
                                )
                            }
                        }

                        val activeVar = uiState.variants.find { it.id == uiState.selectedVariantId }
                        if (activeVar != null && activeVar.descriptionFr.isNotEmpty()) {
                            Text(
                                text = activeVar.descriptionFr,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }

            // Checklist des pièces à fournir
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
                                    text = "Pièces à fournir",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Cochez pour préparer votre dossier",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "${uiState.checkedPieceIds.count { id -> applicablePieces.any { it.id == id } }} / ${applicablePieces.size}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        applicablePieces.forEach { piece ->
                            val isChecked = uiState.checkedPieceIds.contains(piece.id)
                            PieceCheckItem(
                                piece = piece,
                                isChecked = isChecked,
                                onToggle = { viewModel.togglePieceCheck(piece.id) }
                            )
                        }
                    }
                }
            }

            // Étapes numérotées
            if (uiState.steps.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "Étapes du parcours",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            uiState.steps.forEach { step ->
                                StepTimelineItem(step = step)
                            }
                        }
                    }
                }
            }

            // Coûts détaillés & Avertissement anti-surcoût
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Si on vous demande plus...",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Text(
                            text = "Le tarif légal est de ${formatFcfa(proc.officialCostXaf)}. Tout montant supérieur exigé sans quittance officielle est indu. Vous pouvez le signaler anonymement sur DémarchesCam.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        OutlinedButton(
                            onClick = { showReportDialog = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.tertiary),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Signaler un tarif abusif")
                        }
                    }
                }
            }

            // Erreurs fréquentes
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = CamGoldSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Erreurs fréquentes à éviter",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = proc.commonMistakesFr,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Retours d'expérience
            if (uiState.testimonials.isNotEmpty()) {
                item {
                    Text(
                        text = "Retours d'expérience (${uiState.testimonials.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(uiState.testimonials, key = { it.id }) { testimonial ->
                    TestimonialCardItem(testimonial = testimonial)
                }
            }
        }
    }

    // Modal de démarrage d'un dossier
    if (showStartDossierDialog) {
        AlertDialog(
            onDismissRequest = { showStartDossierDialog = false },
            title = { Text("Suivre cette démarche") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Créez votre dossier personnel pour sauvegarder vos pièces cochées, suivre vos dépenses réelles et les dates clés.")
                    Text("Ville de votre démarche :", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Yaoundé", "Douala", "Bafoussam").forEach { city ->
                            FilterChip(
                                selected = selectedCityForDossier == city,
                                onClick = { selectedCityForDossier = city },
                                label = { Text(city) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.createDossierFromThis(selectedCityForDossier) { newId ->
                            showStartDossierDialog = false
                            onDossierCreated(newId)
                        }
                    },
                    modifier = Modifier.testTag("confirm_create_dossier_button")
                ) {
                    Text("Créer mon dossier")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showStartDossierDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    // Modal de signalement d'anomalie
    if (showReportDialog) {
        var reportField by remember { mutableStateOf("PRIX") }
        var costText by remember { mutableStateOf("") }
        var commentText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text("Signaler un écart sur cette fiche") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Votre signalement aide la communauté camerounaise à maintenir des données 100% fiables.")
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("PRIX", "DELAI", "PIECE", "ADRESSE").forEach { f ->
                            FilterChip(
                                selected = reportField == f,
                                onClick = { reportField = f },
                                label = { Text(f, fontSize = 11.sp) }
                            )
                        }
                    }
                    if (reportField == "PRIX") {
                        OutlinedTextField(
                            value = costText,
                            onValueChange = { costText = it },
                            label = { Text("Prix constaté en FCFA") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("report_cost_input")
                        )
                    }
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        label = { Text("Précisions (lieu, détails)") },
                        modifier = Modifier.fillMaxWidth().testTag("report_comment_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cost = costText.toIntOrNull() ?: 0
                        viewModel.submitReport(reportField, cost, commentText)
                        showReportDialog = false
                    },
                    modifier = Modifier.testTag("submit_report_button")
                ) {
                    Text("Envoyer le signalement")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showReportDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    uiState.reportSuccessMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { viewModel.clearReportMessage() },
            title = { Text("Signalement reçu") },
            text = { Text(msg) },
            confirmButton = {
                Button(onClick = { viewModel.clearReportMessage() }) {
                    Text("OK")
                }
            }
        )
    }
}
