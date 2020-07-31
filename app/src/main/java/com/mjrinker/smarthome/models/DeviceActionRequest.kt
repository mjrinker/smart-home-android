package com.mjrinker.smarthome.models

class DeviceActionRequest(
    val nickname: String,
    val actions: ArrayList<DeviceAction>
) {
    override fun toString(): String {
        return "DeviceActionRequest(nickname='$nickname', actions=$actions)"
    }

    fun toJSON(): String {
        val actionsListJSON = (actions.map {it -> "\"${it.action}\":${if (it.value is String) "\"${it.value}\"" else it.value }"})
            .joinToString(prefix = "{", postfix = "}", separator = ",")
        return "{\"nickname\":\"$nickname\",\"actions\":$actionsListJSON}"
    }
}
