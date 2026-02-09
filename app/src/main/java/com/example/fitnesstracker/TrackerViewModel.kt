package com.example.fitux

import androidx.lifecycle.ViewModel

data class TrackerState(
    val isWorkoutOn: Boolean = false,
    val stepsProgress: Float = 0.64f,
    val moveMinutesProgress: Float = 0.42f,
    val heartRateProgress: Float = 0.80f,
)

class TrackerViewModel : ViewModel() {
    var state: TrackerState = TrackerState()
        private set

    fun toggleWorkout() {
        state = state.copy(isWorkoutOn = !state.isWorkoutOn)
    }

    fun handleVoiceCommand(cmd: String) {
        val c = cmd.lowercase()
        state = when {
            "start" in c && ("run" in c || "workout" in c) -> state.copy(isWorkoutOn = true)
            "stop" in c && ("run" in c || "workout" in c) -> state.copy(isWorkoutOn = false)
            else -> state
        }
    }
}