package com.mjrinker.smarthome.models

class Room(
    var label: String,
    var name: String,
    var actions: ArrayList<DeviceAction>
) {
    override fun toString(): String {
        return "Room(label='$label', name=$name actions=$actions)"
    }

    fun toJSON(): String {
        val actionsListJSON = (actions.map {it -> "\"${it.action}\":${if (it.value is String) "\"${it.value}\"" else it.value }"})
            .joinToString(prefix = "{", postfix = "}", separator = ",")
        return "{\"label\":\"$label\",\"name\":$name,\"actions\":${actionsListJSON}}"
    }
}
