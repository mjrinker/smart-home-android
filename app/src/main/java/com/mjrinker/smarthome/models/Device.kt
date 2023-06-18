package com.mjrinker.smarthome.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Device(
    val id: Int?,
    val mfg_id: String?,
    val name: String?,
    val label: String?,
    val platform: String?,
    val type: String?,
    val state: Boolean?,
    val online: Boolean?,
    val light_state: LightState?
) : Parcelable {

    override fun toString(): String {
        return "Device(id='$id', name=$name)"
    }

    fun toJSON(): String {
        val lightStateJson = light_state?.toJSON() ?: "{}";
        return "{\"id\":$id, \"mfg_id\":\"$mfg_id\", \"name\":\"$name\", \"label\":\"$label\", \"platform\":\"$platform\", \"type\":\"$type\", \"state\":$state, \"online\":$online, \"light_state\":$lightStateJson}}"
    }
}
