package com.mjrinker.smarthome.models

class Room(
    var label: String,
    var names: ArrayList<String>,
    var actions: ArrayList<DeviceAction>
) {
    override fun toString(): String {
        return "Room(label='$label', names=$names, actions=$actions)"
    }

    fun toJSON(): String {
        val namesListString = names.joinToString(prefix = "[\"", postfix = "\"]", separator = "\",\"")
        val actionsListJSON = (actions.map {it -> "\"${it.action}\":${if (it.value is String) "\"${it.value}\"" else it.value }"})
            .joinToString(prefix = "{", postfix = "}", separator = ",")
        return "{\"label\":\"$label\",\"names\":$namesListString,\"actions\":${actionsListJSON}}"
    }
}
