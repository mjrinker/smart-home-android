package com.mjrinker.smarthome

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mjrinker.smarthome.adapters.RoomRecyclerAdapter
import com.mjrinker.smarthome.models.DeviceAction
import com.mjrinker.smarthome.models.Room
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import okhttp3.*
import java.io.IOException

class DataSource {
    private val TAG = "DataSource"

    private val client = OkHttpClient()

    fun loadRooms(roomAdapter: RoomRecyclerAdapter): ArrayList<Room>? {
        var rooms: ArrayList<Room>? = null
        val url = "http://192.168.0.107:3031/rooms"
        val request = Request.Builder()
            .url(url)
            .header("X-ApiVersion", "2.0.0")
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) { println(e) }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseBody = response.body()
                    val responseBodyString = responseBody?.string()
                    println("$TAG->getRooms: $responseBodyString")
                    rooms = JsonPath.parse(responseBodyString)?.read("$.rooms")
                    if (rooms !== null && rooms?.size!! > 0) {
                        rooms?.add(
                            0, Room(
                                "All",
                                "*bulb",
                                arrayListOf(
                                    DeviceAction("Off", true),
                                    DeviceAction("On", true)
                                )
                            )
                        )
                    }
                }
            }
        })

        while (rooms === null) {
            Log.d(TAG, "loadRooms: null")
        }

        roomAdapter.submitList(rooms!!)
        return rooms
    }
}
