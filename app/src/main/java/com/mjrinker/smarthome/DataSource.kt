package com.mjrinker.smarthome

import com.mjrinker.smarthome.models.DeviceAction
import com.mjrinker.smarthome.models.Room

class DataSource {
    companion object {
        fun createDataSet(): ArrayList<Room> {
            val list = ArrayList<Room>()
            list.add(
                Room(
                    "Ashlee's Office",
                    arrayListOf("Ashlee's Office"),
                    arrayListOf(DeviceAction("Off", true), DeviceAction("On", true))
                )
            )
            list.add(
                Room(
                    "Closet",
                    arrayListOf("Closet"),
                    arrayListOf(DeviceAction("Off", true), DeviceAction("On", true))
                )
            )
            list.add(
                Room(
                    "Dining Room",
                    arrayListOf("Dining Room"),
                    arrayListOf(DeviceAction("Off", true), DeviceAction("On", true))
                )
            )
            list.add(
                Room(
                    "Hallway",
                    arrayListOf("Hallway"),
                    arrayListOf(DeviceAction("Off", true), DeviceAction("On", true))
                )
            )
            list.add(
                Room(
                    "Kitchen",
                    arrayListOf("Kitchen"),
                    arrayListOf(DeviceAction("Off", true), DeviceAction("On", true))
                )
            )
            list.add(
                Room(
                    "Living Room",
                    arrayListOf("Living Room"),
                    arrayListOf(DeviceAction("Off", true), DeviceAction("On", true))
                )
            )
            list.add(
                Room(
                    "Master Bathroom",
                    arrayListOf("Master Bathroom"),
                    arrayListOf(DeviceAction("Off", true), DeviceAction("On", true))
                )
            )
            list.add(
                Room(
                    "Master Bedroom",
                    arrayListOf("Master Bedroom"),
                    arrayListOf(DeviceAction("Off", true), DeviceAction("On", true))
                )
            )
            list.add(
                Room(
                    "Master Bedroom Lamp",
                    arrayListOf("Master Bedroom Lamp"),
                    arrayListOf(DeviceAction("Off", true), DeviceAction("On", true))
                )
            )
            list.add(
                Room(
                    "Matt's Office",
                    arrayListOf("Matt's Office"),
                    arrayListOf(DeviceAction("Off", true), DeviceAction("On", true))
                )
            )
            list.add(
                Room(
                    "Vanity",
                    arrayListOf("Vanity"),
                    arrayListOf(DeviceAction("Off", true), DeviceAction("On", true))
                )
            )
            return list
        }
    }
}
