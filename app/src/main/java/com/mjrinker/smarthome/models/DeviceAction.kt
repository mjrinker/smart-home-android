package com.mjrinker.smarthome.models

class DeviceAction(
    val action: String,
    val value: Any
) {
    override fun toString(): String {
        return "DeviceAction(action='$action', value=$value)"
    }

    fun toJSON(): String {
        return "{\"$action\":${if (value is String) "\"$value\"" else value }}"
    }
}
