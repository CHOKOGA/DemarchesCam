package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.repository.DemarchesRepository
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.CommunityScreen
import com.example.ui.screens.DossierDetailScreen
import com.example.ui.screens.DossierListScreen
import com.example.ui.screens.FamilyProfilesScreen
import com.example.ui.screens.LocationScreen
import com.example.ui.screens.NewDossierScreen
import com.example.ui.screens.ProcedureDetailScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.DemarchesCamTheme
import com.example.ui.viewmodel.CatalogViewModel
import com.example.ui.viewmodel.CommunityViewModel
import com.example.ui.viewmodel.DossierViewModel
import com.example.ui.viewmodel.LocationViewModel
import com.example.ui.viewmodel.ProcedureDetailViewModel

enum class MainNavTab {
    CATALOG,
    DOSSIERS,
    LOCATIONS,
    COMMUNITY
}

sealed class AppScreen {
    object Main : AppScreen()
    data class ProcedureDetail(val procedureId: String) : AppScreen()
    data class DossierDetail(val dossierId: Long) : AppScreen()
    object NewDossier : AppScreen()
    object FamilyProfiles : AppScreen()
    object Auth : AppScreen()
    object Settings : AppScreen()
    object About : AppScreen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = DemarchesRepository(database)

        setContent {
            DemarchesCamTheme {
                DemarchesCamApp(repository = repository)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemarchesCamApp(repository: DemarchesRepository) {
    var currentTab by remember { mutableStateOf(MainNavTab.CATALOG) }
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Main) }

    val userSession by repository.userSession.collectAsStateWithLifecycle(initialValue = com.example.data.repository.UserSession())

    val catalogViewModel: CatalogViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CatalogViewModel(repository) as T
            }
        }
    )

    val dossierViewModel: DossierViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DossierViewModel(repository) as T
            }
        }
    )

    val locationViewModel: LocationViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return LocationViewModel(repository) as T
            }
        }
    )

    val communityViewModel: CommunityViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CommunityViewModel(repository) as T
            }
        }
    )

    // BackHandler pour les sous-écrans
    if (currentScreen !is AppScreen.Main) {
        BackHandler {
            currentScreen = AppScreen.Main
        }
    }

    when (val screen = currentScreen) {
        is AppScreen.ProcedureDetail -> {
            val detailViewModel: ProcedureDetailViewModel = viewModel(
                key = screen.procedureId,
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return ProcedureDetailViewModel(repository, screen.procedureId) as T
                    }
                }
            )
            ProcedureDetailScreen(
                viewModel = detailViewModel,
                repository = repository,
                onBack = { currentScreen = AppScreen.Main },
                onDossierCreated = { newDossierId ->
                    currentScreen = AppScreen.DossierDetail(newDossierId)
                }
            )
        }

        is AppScreen.DossierDetail -> {
            DossierDetailScreen(
                viewModel = dossierViewModel,
                dossierId = screen.dossierId,
                onBack = { currentScreen = AppScreen.Main },
                onFinishDossierWithTestimonial = { procId, procTitle, city ->
                    currentTab = MainNavTab.COMMUNITY
                    currentScreen = AppScreen.Main
                }
            )
        }

        is AppScreen.NewDossier -> {
            NewDossierScreen(
                dossierViewModel = dossierViewModel,
                repository = repository,
                onBack = { currentScreen = AppScreen.Main },
                onDossierCreated = { newDossierId ->
                    currentScreen = AppScreen.DossierDetail(newDossierId)
                }
            )
        }

        is AppScreen.FamilyProfiles -> {
            FamilyProfilesScreen(
                repository = repository,
                onBack = { currentScreen = AppScreen.Main }
            )
        }

        is AppScreen.Auth -> {
            AuthScreen(
                repository = repository,
                onBack = { currentScreen = AppScreen.Main },
                onAuthSuccess = { currentScreen = AppScreen.Main }
            )
        }

        is AppScreen.Settings -> {
            SettingsScreen(
                repository = repository,
                catalogViewModel = catalogViewModel,
                onBack = { currentScreen = AppScreen.Main }
            )
        }

        is AppScreen.About -> {
            AboutScreen(onBack = { currentScreen = AppScreen.Main })
        }

        is AppScreen.Main -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "DémarchesCam",
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        actions = {
                            IconButton(
                                onClick = { currentScreen = AppScreen.FamilyProfiles },
                                modifier = Modifier.testTag("open_family_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FamilyRestroom,
                                    contentDescription = "Mes Proches",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(
                                onClick = { currentScreen = AppScreen.Auth },
                                modifier = Modifier.testTag("open_auth_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = if (userSession.isLoggedIn) "Mon Compte" else "Se connecter",
                                    tint = if (userSession.isLoggedIn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                            }
                            IconButton(
                                onClick = { currentScreen = AppScreen.Settings },
                                modifier = Modifier.testTag("open_settings_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Paramètres",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(
                                onClick = { currentScreen = AppScreen.About },
                                modifier = Modifier.testTag("open_about_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "À propos & Fiabilité",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("main_bottom_nav")
                    ) {
                        NavigationBarItem(
                            selected = currentTab == MainNavTab.CATALOG,
                            onClick = { currentTab = MainNavTab.CATALOG },
                            icon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Catalogue") },
                            label = { Text("Démarches") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("nav_tab_catalog")
                        )
                        NavigationBarItem(
                            selected = currentTab == MainNavTab.DOSSIERS,
                            onClick = { currentTab = MainNavTab.DOSSIERS },
                            icon = { Icon(imageVector = Icons.Default.Assignment, contentDescription = "Mon Espace") },
                            label = { Text("Mon Espace") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("nav_tab_dossiers")
                        )
                        NavigationBarItem(
                            selected = currentTab == MainNavTab.LOCATIONS,
                            onClick = { currentTab = MainNavTab.LOCATIONS },
                            icon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Lieux") },
                            label = { Text("Lieux") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("nav_tab_locations")
                        )
                        NavigationBarItem(
                            selected = currentTab == MainNavTab.COMMUNITY,
                            onClick = { currentTab = MainNavTab.COMMUNITY },
                            icon = { Icon(imageVector = Icons.Default.RateReview, contentDescription = "Avis") },
                            label = { Text("Avis") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("nav_tab_community")
                        )
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    when (currentTab) {
                        MainNavTab.CATALOG -> {
                            CatalogScreen(
                                viewModel = catalogViewModel,
                                onProcedureClick = { procedureId ->
                                    currentScreen = AppScreen.ProcedureDetail(procedureId)
                                }
                            )
                        }
                        MainNavTab.DOSSIERS -> {
                            DossierListScreen(
                                viewModel = dossierViewModel,
                                onDossierClick = { dossierId ->
                                    currentScreen = AppScreen.DossierDetail(dossierId)
                                },
                                onNewDossierClick = {
                                    currentScreen = AppScreen.NewDossier
                                }
                            )
                        }
                        MainNavTab.LOCATIONS -> {
                            LocationScreen(viewModel = locationViewModel)
                        }
                        MainNavTab.COMMUNITY -> {
                            CommunityScreen(viewModel = communityViewModel)
                        }
                    }
                }
            }
        }
    }
}
