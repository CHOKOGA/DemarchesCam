package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.data.local.entity.LocationEntity
import com.example.ui.components.TrustDisclaimerBanner
import com.example.ui.theme.CamGoldSecondary
import com.example.ui.viewmodel.LocationViewModel

@Composable
fun LocationScreen(
    viewModel: LocationViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val locations by viewModel.locations.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val cities = listOf("Toutes", "Yaoundé", "Douala", "Bafoussam")
    val categories = listOf("TOUTES", "POLICE", "MAIRIE", "TRIBUNAL", "CFCE")

    var selectedLocDetail by remember { mutableStateOf<LocationEntity?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("location_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Lieux & Services Administratifs",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Adresses, horaires et meilleures heures pour éviter l'affluence au guichet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            TrustDisclaimerBanner()
        }

        // Filtre par ville
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Filtrer par ville :", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    cities.forEach { city ->
                        FilterChip(
                            selected = uiState.selectedCity == city,
                            onClick = { viewModel.onCitySelect(city) },
                            label = { Text(city) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.testTag("location_city_chip_$city")
                        )
                    }
                }
            }
        }

        // Filtre par type d'administration
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val label = when (cat) {
                        "TOUTES" -> "Toutes administrations"
                        "POLICE" -> "Police / DGSN (CNI, Passeport)"
                        "MAIRIE" -> "Mairies (État Civil)"
                        "TRIBUNAL" -> "Tribunaux (Casier, Nationalité)"
                        "CFCE" -> "CFCE (Entreprises)"
                        else -> cat
                    }
                    FilterChip(
                        selected = uiState.selectedCategory == cat,
                        onClick = { viewModel.onCategorySelect(cat) },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        modifier = Modifier.testTag("location_category_chip_$cat")
                    )
                }
            }
        }

        // Liste des lieux
        items(locations, key = { it.id }) { loc ->
            LocationCardItem(
                location = loc,
                onClick = { selectedLocDetail = loc },
                onNavigate = {
                    val gmmIntentUri = Uri.parse("geo:${loc.latitude},${loc.longitude}?q=${Uri.encode(loc.name)}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    try {
                        context.startActivity(mapIntent)
                    } catch (e: Exception) {
                        // Fallback web maps
                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=${loc.latitude},${loc.longitude}"))
                        context.startActivity(webIntent)
                    }
                },
                onCall = {
                    val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${loc.phone.replace(" ", "")}"))
                    try {
                        context.startActivity(callIntent)
                    } catch (_: Exception) {}
                }
            )
        }
    }

    // Modal détail du lieu
    selectedLocDetail?.let { loc ->
        AlertDialog(
            onDismissRequest = { selectedLocDetail = null },
            title = {
                Text(loc.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("📍 ${loc.district}, ${loc.city}")
                    Text("Adresse : ${loc.address}", style = MaterialTheme.typography.bodySmall)
                    Text("🕒 Horaires : ${loc.openingHours}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Text("📞 Téléphone : ${loc.phone}", style = MaterialTheme.typography.bodySmall)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("💡 Créneau conseillé :", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(loc.bestTimeToVisit, fontSize = 12.sp)
                        }
                    }
                    Text("Démarches traitées : ${loc.servicesOffered}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = {
                Button(onClick = { selectedLocDetail = null }) {
                    Text("Fermer")
                }
            }
        )
    }
}

@Composable
fun LocationCardItem(
    location: LocationEntity,
    onClick: () -> Unit,
    onNavigate: () -> Unit,
    onCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .testTag("location_card_${location.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${location.city} • ${location.district}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = location.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = location.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = location.address,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = location.openingHours,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Meilleur créneau
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "💡 ${location.bestTimeToVisit}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Actions Itinéraire et Appel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (location.phone.isNotEmpty()) {
                    OutlinedButton(
                        onClick = onCall,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Appeler", fontSize = 12.sp)
                    }
                }
                Button(
                    onClick = onNavigate,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(imageVector = Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Itinéraire", fontSize = 12.sp)
                }
            }
        }
    }
}
