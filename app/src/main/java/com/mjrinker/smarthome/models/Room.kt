package com.mjrinker.smarthome.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class Room(
    var id: Int?,
    var label: String?,
    var name: String?,
    var active: Boolean?,
    var order: Int?,
    var state: Boolean?,
    var actions: @RawValue ArrayList<DeviceAction>,
    var devices: @RawValue ArrayList<Device>
) : Parcelable {
    override fun toString(): String {
        return "Room(id=$id, label='$label', name=$name)"
    }

    fun toJSON(): String {
        val actionsListJSON = (actions.map {it -> "\"${it.action}\":${if (it.value is String) "\"${it.value}\"" else it.value }"})
            .joinToString(prefix = "{", postfix = "}", separator = ",")
        val devicesListJSON = (devices.map {it -> it.toJSON()})
            .joinToString(prefix = "{", postfix = "}", separator = ",")
        return "{\"label\":\"$label\",\"name\":\"$name\",\"active\":$active,\"order\":$order,\"state\":$state,\"actions\":${actionsListJSON},\"devices\":${devicesListJSON}}"
    }
}
