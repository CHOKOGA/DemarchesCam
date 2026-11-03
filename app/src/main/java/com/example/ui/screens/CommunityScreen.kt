package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.TestimonialCardItem
import com.example.ui.components.TrustDisclaimerBanner
import com.example.ui.theme.CamGoldSecondary
import com.example.ui.viewmodel.CommunityViewModel

@Composable
fun CommunityScreen(
    viewModel: CommunityViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val testimonials by viewModel.testimonials.collectAsStateWithLifecycle()
    val reports by viewModel.reports.collectAsStateWithLifecycle()

    var showSubmitDialog by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf("TEMOIGNAGES") } // TEMOIGNAGES or SIGNALEMENTS

    val filteredTestimonials = testimonials.filter {
        val matchesCity = if (uiState.filterCity == "Toutes") true else it.city == uiState.filterCity
        val matchesProc = if (uiState.filterProcedureId == "TOUTES") true else it.procedureId == uiState.filterProcedureId
        matchesCity && matchesProc
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSubmitDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("fab_new_testimonial")
            ) {
                Icon(imageVector = Icons.Default.RateReview, contentDescription = "Publier un retour")
            }
        },
        modifier = modifier.testTag("community_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Témoignages & Transparence",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Partage d'expériences réelles : délais constatés, coûts payés et signalements.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                TrustDisclaimerBanner()
            }

            // Onglets Témoignages / Signalements
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = activeTab == "TEMOIGNAGES",
                        onClick = { activeTab = "TEMOIGNAGES" },
                        label = { Text("Témoignages (${testimonials.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.weight(1f).testTag("tab_testimonials")
                    )
                    FilterChip(
                        selected = activeTab == "SIGNALEMENTS",
                        onClick = { activeTab = "SIGNALEMENTS" },
                        label = { Text("Alertes & Écarts (${reports.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                            selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                        ),
                        modifier = Modifier.weight(1f).testTag("tab_reports")
                    )
                }
            }

            if (activeTab == "TEMOIGNAGES") {
                // Filtre par ville
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Toutes", "Yaoundé", "Douala", "Bafoussam").forEach { city ->
                            FilterChip(
                                selected = uiState.filterCity == city,
                                onClick = { viewModel.onCityFilter(city) },
                                label = { Text(city, fontSize = 12.sp) }
                            )
                        }
                    }
                }

                if (filteredTestimonials.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Aucun témoignage pour ce filtre.")
                                Text("Soyez le premier à partager votre expérience au guichet !", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                } else {
                    items(filteredTestimonials, key = { it.id }) { item ->
                        TestimonialCardItem(
                            testimonial = item,
                            onHelpfulClick = { viewModel.markHelpful(item.id) }
                        )
                    }
                }
            } else {
                // Section Signalements
                if (reports.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Box(modifier = Modifier.padding(24.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text("Aucun signalement d'anomalie en cours.")
                            }
                        }
                    }
                } else {
                    items(reports, key = { it.id }) { report ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = report.procedureTitle,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.tertiaryContainer
                                    ) {
                                        Text(
                                            text = report.status,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Champ signalé : ${report.fieldReported}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.Bold
                                )
                                if (report.userCostReportedXaf > 0) {
                                    Text(
                                        text = "Montant exigé constaté : ${report.userCostReportedXaf} FCFA",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                }
                                Text(
                                    text = report.userComment,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Publier un témoignage
    if (showSubmitDialog) {
        var procId by remember { mutableStateOf("cni") }
        var procTitle by remember { mutableStateOf("Carte Nationale d'Identité (CNI)") }
        var city by remember { mutableStateOf("Yaoundé") }
        var serviceName by remember { mutableStateOf("") }
        var delayDaysText by remember { mutableStateOf("") }
        var costPaidText by remember { mutableStateOf("") }
        var rating by remember { mutableIntStateOf(5) }
        var comment by remember { mutableStateOf("") }
        var author by remember { mutableStateOf("") }
        var isAnonymous by remember { mutableStateOf(false) }

        val procedureOptions = listOf(
            "cni" to "Carte Nationale d'Identité (CNI)",
            "passeport" to "Passeport Biométrique Ordinaire",
            "acte_naissance" to "Acte de Naissance",
            "casier_judiciaire" to "Casier Judiciaire",
            "creation_entreprise" to "Création d'Entreprise (CFCE)"
        )

        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = { Text("Partager votre expérience") },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text("Démarche concernée :", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            procedureOptions.forEach { (id, title) ->
                                FilterChip(
                                    selected = procId == id,
                                    onClick = {
                                        procId = id
                                        procTitle = title
                                    },
                                    label = { Text(title.take(18) + "...", fontSize = 11.sp) }
                                )
                            }
                        }
                    }

                    item {
                        Text("Ville :", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Yaoundé", "Douala", "Bafoussam").forEach { c ->
                                FilterChip(
                                    selected = city == c,
                                    onClick = { city = c },
                                    label = { Text(c, fontSize = 11.sp) }
                                )
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = serviceName,
                            onValueChange = { serviceName = it },
                            label = { Text("Service visité (ex: Commissariat 1er, TPI)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("service_name_input")
                        )
                    }

                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = delayDaysText,
                                onValueChange = { delayDaysText = it },
                                label = { Text("Délai réel (jours)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("delay_input")
                            )
                            OutlinedTextField(
                                value = costPaidText,
                                onValueChange = { costPaidText = it },
                                label = { Text("Coût payé (FCFA)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("cost_input")
                            )
                        }
                    }

                    item {
                        Text("Votre note de satisfaction :", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            (1..5).forEach { star ->
                                IconButton(onClick = { rating = star }) {
                                    Icon(
                                        imageVector = if (star <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "$star étoiles",
                                        tint = CamGoldSecondary
                                    )
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            label = { Text("Commentaire & conseils pour les autres") },
                            modifier = Modifier.fillMaxWidth().testTag("comment_input")
                        )
                        Text(
                            text = "Note : les noms propres d'agents et numéros sont masqués automatiquement pour préserver la sérénité du service public.",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Publier de manière anonyme", fontSize = 12.sp)
                            Switch(checked = isAnonymous, onCheckedChange = { isAnonymous = it })
                        }
                    }

                    if (!isAnonymous) {
                        item {
                            OutlinedTextField(
                                value = author,
                                onValueChange = { author = it },
                                label = { Text("Votre prénom ou pseudo (ex: Paul K.)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val days = delayDaysText.toIntOrNull() ?: 1
                        val cost = costPaidText.toIntOrNull() ?: 0
                        viewModel.submitTestimonial(
                            procedureId = procId,
                            procedureTitle = procTitle,
                            city = city,
                            serviceName = serviceName.ifEmpty { "Guichet public" },
                            delayDays = days,
                            costPaidXaf = cost,
                            rating = rating,
                            comment = comment,
                            author = author.ifEmpty { "Citoyen camerounais" },
                            isAnonymous = isAnonymous
                        )
                        showSubmitDialog = false
                    },
                    modifier = Modifier.testTag("submit_testimonial_button")
                ) {
                    Text("Publier")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSubmitDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    uiState.submitSuccessMsg?.let { msg ->
        AlertDialog(
            onDismissRequest = { viewModel.clearSuccessMsg() },
            title = { Text("Publication confirmée") },
            text = { Text(msg) },
            confirmButton = {
                Button(onClick = { viewModel.clearSuccessMsg() }) {
                    Text("OK")
                }
            }
        )
    }
}
