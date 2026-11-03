package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.DemarchesRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    repository: DemarchesRepository,
    onBack: () -> Unit,
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val session by repository.userSession.collectAsStateWithLifecycle(initialValue = com.example.data.repository.UserSession())

    var step by remember { mutableIntStateOf(1) } // 1: Téléphone, 2: Code OTP
    var phoneInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var otpInput by remember { mutableStateOf("") }
    var simulatedGeneratedOtp by remember { mutableStateOf("2486") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (session.isLoggedIn) "Mon Compte" else "Connexion Citoyenne", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("auth_back_button")) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        modifier = modifier.testTag("auth_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (session.isLoggedIn) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Compte connecté", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Text("Nom : ${session.userName.ifEmpty { "Citoyen camerounais" }}", fontWeight = FontWeight.SemiBold)
                        Text("Téléphone : ${session.phoneNumber}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                repository.logout()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth().testTag("logout_button")
                        ) {
                            Text("Se déconnecter")
                        }
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Connexion rapide sans mot de passe : votre numéro vous permet de retrouver vos dossiers et vos proches.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                if (step == 1) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Votre prénom ou nom") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("auth_name_input")
                    )

                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("Numéro de téléphone (ex: 699 00 00 00)") },
                        leadingIcon = {
                            Text("🇨🇲 +237 ", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("auth_phone_input")
                    )

                    Button(
                        onClick = {
                            if (phoneInput.length >= 8) {
                                step = 2
                                errorMessage = null
                            } else {
                                errorMessage = "Veuillez entrer un numéro de téléphone valide."
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("send_otp_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Recevoir le code SMS (OTP)")
                    }
                } else {
                    Text(
                        text = "Entrez le code reçu au +237 $phoneInput",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Code SMS simulé : $simulatedGeneratedOtp",
                            modifier = Modifier.padding(10.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    OutlinedTextField(
                        value = otpInput,
                        onValueChange = { otpInput = it },
                        label = { Text("Code à 4 chiffres") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("otp_code_input")
                    )

                    Button(
                        onClick = {
                            if (otpInput.trim() == simulatedGeneratedOtp || otpInput.trim() == "1234") {
                                repository.login("+237 $phoneInput", nameInput.ifEmpty { "Citoyen" })
                                onAuthSuccess()
                            } else {
                                errorMessage = "Code invalide. Entrez le code $simulatedGeneratedOtp."
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("verify_otp_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Confirmer et me connecter")
                    }

                    TextButton(onClick = { step = 1 }) {
                        Text("Modifier le numéro")
                    }
                }

                errorMessage?.let { err ->
                    Text(text = err, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                    Text("Continuer en mode visiteur")
                }
            }
        }
    }
}
