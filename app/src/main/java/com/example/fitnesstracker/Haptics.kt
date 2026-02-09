package com.example.fitux

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object Haptics {
    private fun vibrator(context: Context): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
    fun patternNudge(ctx: Context) = vibrate(ctx, longArrayOf(0, 40))
    fun patternAchievement(ctx: Context) = vibrate(ctx, longArrayOf(0, 60, 80, 140, 80, 200))
    fun patternHydration(ctx: Context) = vibrate(ctx, longArrayOf(0, 30, 60, 30))
    private fun vibrate(ctx: Context, timings: LongArray) {
        val vib = vibrator(ctx)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) vib.vibrate(VibrationEffect.createWaveform(timings, -1))
        else @Suppress("DEPRECATION") vib.vibrate(timings, -1)
    }
}