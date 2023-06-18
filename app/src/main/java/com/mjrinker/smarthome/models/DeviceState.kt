package com.mjrinker.smarthome.models

import com.mjrinker.smarthome.SmartHomeAPI

class DeviceState(
    val id: Int?,
    val name: String,
    val online: Boolean,
    val state: Boolean,
    val light_state: LightState?
) {
    override fun toString(): String {
//        return "DeviceState(name='$name', online=$online, state=$state, light_state=$light_state)"
        return "DeviceState(id=$id, name='$name', online=$online, state=$state, light_state=$light_state)"
    }

    fun toJSON(): String {
//        return "{\"name\":\"$name\",\"online\":$online,\"state\":$state,\"light_state\":${light_state?.toJSON()}}"
        return "{\"id\":$id,\"name\":\"$name\",\"online\":$online,\"state\":$state,\"light_state\":${light_state?.toJSON()}}"
    }

    companion object {
        private const val TAG = "DeviceState"

        fun roomIsOn(room: Room, deviceStates: ArrayList<DeviceState>, forceGetStates: Boolean = true): Boolean {
            if (forceGetStates) {
                getDeviceStates(room, deviceStates)
                while (deviceStates.isEmpty()) {}
            } else if (deviceStates.isEmpty()) {
                return false
            }

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
