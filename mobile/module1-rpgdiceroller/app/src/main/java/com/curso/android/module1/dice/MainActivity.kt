package com.curso.android.module1.dice

// =============================================================================
// IMPORTACIONES
// --- Android Core ---
import android.os.Bundle

// --- AndroidX Activity ---
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

// --- Jetpack Compose Core & Layouts ---
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

// --- Material 3 Components ---
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

// --- Compose Runtime (Estado y Efectos) ---
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

// --- Compose UI ---
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Kotlin Coroutines ---
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// --- APP Background ---
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale

// =============================================================================
// ACTIVIDAD PRINCIPAL
// =============================================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF759576)) {
                    CharacterCreationScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterCreationScreen() {
    var vit by rememberSaveable { mutableIntStateOf(10) }
    var dex by rememberSaveable { mutableIntStateOf(10) }
    var wis by rememberSaveable { mutableIntStateOf(10) }

    val coroutineScope = rememberCoroutineScope()
    val total = vit + dex + wis

    fun rollStat(onUpdate: (Int) -> Unit) {
        coroutineScope.launch {
            repeat(10) {
                onUpdate((1..20).random()) // dado de 20
                delay(50)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = { TopAppBar(title = { Text("Character Creator") },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors( Color(0xFFB9C9B9))) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatRow("VITALITY", vit) { rollStat { vit = it } }
                StatRow("DEXTERITY", dex) { rollStat { dex = it } }
                StatRow("WISDOM", wis) { rollStat { wis = it } }

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFB9C9B9)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("TOTAL SCORE", style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        Text("$total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)

                        val (message, color) = when {
                            total >= 50 -> "Godlike!" to Color(0xFFFFDF33)
                            total < 30 -> "Re-roll recommended!" to Color.Red
                            else -> "Balanced Hero" to Color.Gray
                        }

                        Text(
                            text = message,
                            color = color,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatRow(name: String, value: Int, onRoll: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))

            Text(
                text = value.toString(),
                fontSize = 24.sp,
                color = Color(0xFF275A29),
                fontWeight = FontWeight.ExtraBold
            )

            Button(
                onClick = onRoll,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF275A29), contentColor = Color.White),
                ) {
                Text("ROLL")
            }
        }
    }
}