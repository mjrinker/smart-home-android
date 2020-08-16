package com.mjrinker.smarthome.models

class LightState(
    val brightness: Int,
    val color: String,
    val temperature: Int
) {
    override fun toString(): String {
        return "DeviceState(brightness=$brightness, color='$color', temperature=$temperature)"
    }

    fun toJSON(): String {
        return "{\"brightness\":$brightness,\"color\":\"$color\",\"temperature\":$temperature}"
    }
}
