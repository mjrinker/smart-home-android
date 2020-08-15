package com.mjrinker.smarthome.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class DeviceAction(
    val action: String?,
    val value: @RawValue Any?
) : Parcelable {

    override fun toString(): String {
        return "DeviceAction(action='$action', value=$value)"
    }

    fun toJSON(): String {
        return "{\"$action\":${if (value is String) "\"$value\"" else value }}"
    }
}
