package com.example.fitux

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.CircularProgressIndicator
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@Composable
fun App(vm: TrackerViewModel = viewModel()) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    var voiceText by remember { mutableStateOf<String?>(null) }
    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (res.resultCode == Activity.RESULT_OK) {
            val data = res.data
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spoken = results?.firstOrNull()
            voiceText = spoken
            vm.handleVoiceCommand(spoken ?: "")
        }
    }

    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Daily Activity", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Rings(vm)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { vm.toggleWorkout() }) { Text(if (vm.state.isWorkoutOn) "Stop" else "Start") }
                    Button(onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a command: 'start run', 'stop workout', 'high contrast'")
                        }
                        launcher.launch(intent)
                    }) { Text("Voice") }
                    Button(onClick = { Haptics.patternAchievement(ctx) }) { Text("Haptic") }
                }
                voiceText?.let { Text("Heard: " + it, fontSize = 12.sp) }
            }
        }
    }
}

@Composable
fun Rings(vm: TrackerViewModel) {
    val st = vm.state
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Ring("Steps", st.stepsProgress)
        Ring("Move", st.moveMinutesProgress)
        Ring("HR", st.heartRateProgress)
    }
}

@Composable
fun Ring(label: String, progress: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(progress = progress, modifier = Modifier.size(72.dp))
        Spacer(Modifier.height(6.dp))
        Text(label)
    }
}