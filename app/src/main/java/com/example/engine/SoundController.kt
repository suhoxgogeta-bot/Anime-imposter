package com.example.engine

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class SoundController(private val context: Context) {

    private var toneGen: ToneGenerator? = null
    var isSoundEnabled: Boolean = true
    var isHapticsEnabled: Boolean = true

    init {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (_: Exception) {
            toneGen = null
        }
    }

    fun playClick() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 40)
        } catch (_: Exception) {}
        vibrate(30)
    }

    fun playTimerTick() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_CDMA_PIP, 40)
        } catch (_: Exception) {}
    }

    fun playTurnNotification() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 120)
        } catch (_: Exception) {}
        vibrate(80)
    }

    fun playClueSubmitted() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 100)
        } catch (_: Exception) {}
        vibrate(50)
    }

    fun playVotingTension() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_CDMA_ALERT_AUTOREDIAL_LITE, 150)
        } catch (_: Exception) {}
        vibrate(100)
    }

    fun playElimination() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 300)
        } catch (_: Exception) {}
        vibrate(200)
    }

    fun playVictory() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_PROMPT, 400)
        } catch (_: Exception) {}
        vibrate(150)
    }

    private fun vibrate(durationMs: Long) {
        if (!isHapticsEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {}
    }

    fun release() {
        try {
            toneGen?.release()
            toneGen = null
        } catch (_: Exception) {}
    }
}
