package com.mjrinker.smarthome.models

import com.mjrinker.smarthome.SmartHomeAPI

class DeviceState(
    val name: String,
    val online: Boolean,
    val state: Boolean,
    val light_state: LightState?
) {
    override fun toString(): String {
        return "DeviceState(name='$name', online=$online, state=$state, light_state=$light_state)"
    }

    fun toJSON(): String {
        return "{\"name\":\"$name\",\"online\":$online,\"state\":$state,\"light_state\":${light_state?.toJSON()}}"
    }

    companion object {
        private const val TAG = "DeviceState"

        fun roomIsOn(room: Room, deviceStates: ArrayList<DeviceState>): Boolean {
            getDeviceStates(room, deviceStates)
            while (deviceStates.size == 0) {}
            var isOn = false
            for (deviceState in deviceStates) {
                isOn = isOn or deviceState.state
                if (isOn) {
                    break
                }
            }
            return isOn
        }

        fun getDeviceStates(room: Room, deviceStates: ArrayList<DeviceState>) {
            deviceStates.clear()
            SmartHomeAPI("192.168.0.107", 3030).getDeviceState(
                    arrayListOf(room.name),
                    deviceStates
            ).send()
        }
    }
}
