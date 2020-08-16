package com.mjrinker.smarthome.models

class DeviceState(
    val name: String,
    val online: Boolean,
    val state: Boolean,
    val light_state: LightState
) {
    override fun toString(): String {
        return "DeviceState(name='$name', online=$online, state=$state, light_state=$light_state)"
    }

    fun toJSON(): String {
        return "{\"name\":\"$name\",\"online\":$online,\"state\":$state,\"light_state\":${light_state.toJSON()}}"
    }
}
