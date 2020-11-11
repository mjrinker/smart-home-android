package com.mjrinker.smarthome.models

import android.util.Log
import androidx.core.graphics.toColorInt
import com.mjrinker.smarthome.util.ColorHelper

class LightState(
    val brightness: Int,
    val color: String,
    val temperature: Int
) {
    val gradientColors = arrayListOf("#ff0000", "#ff8800", "#ffff00", "#88ff00", "#00ff00", "#00ff88", "#00ffff", "#0088ff", "#0000ff", "#8800ff", "#ff00ff", "#ff0088")
    val colorInts = ArrayList<Int>(gradientColors.map { it -> it.toColorInt() })

    override fun toString(): String {
        return "DeviceState(brightness=$brightness, color='$color', temperature=$temperature)"
    }

    fun colorPercent(): Float {
        var colorHex = color
        when (colorHex.length) {
            3 -> {
                colorHex = colorHex.substring(0, 1).repeat(2) + colorHex.substring(1, 2).repeat(2) + colorHex.substring(2, 3).repeat(2)
            }
            4 -> {
                colorHex = colorHex.substring(1, 2).repeat(2) + colorHex.substring(2, 3).repeat(2) + colorHex.substring(3, 4).repeat(2)
            }
            8 -> {
                colorHex = colorHex.substring(2)
            }
        }

        for (num in 0..10000) {
            val percent = num.toDouble() / 10000
            val blendColor = ColorHelper.blendColors(percent, colorInts, "0xRGB")
            if (blendColor == colorHex) {
                return (num.toDouble() / 100).toFloat()
            }
        }
        return 0f
    }

    fun toJSON(): String {
        return "{\"brightness\":$brightness,\"color\":\"$color\",\"temperature\":$temperature}"
    }
}
