package com.mjrinker.smarthome

import com.mjrinker.smarthome.adapters.RoomRecyclerAdapter
import com.mjrinker.smarthome.models.Room

class DataSource {
    private val TAG = "DataSource"

    fun loadRooms(roomAdapter: RoomRecyclerAdapter): ArrayList<Room>? {
        val rooms: ArrayList<Room> = arrayListOf()
        SmartHomeAPI("192.168.0.107", 3030).getRooms(rooms).send()

        while (rooms.size == 0) {}

        roomAdapter.submitList(rooms)
        return rooms
    }
}
