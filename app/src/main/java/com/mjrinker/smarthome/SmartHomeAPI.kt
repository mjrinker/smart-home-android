package com.mjrinker.smarthome

import android.util.Log
import com.google.gson.Gson
import com.mjrinker.smarthome.models.DeviceAction
import com.mjrinker.smarthome.models.DeviceActionRequest
import com.mjrinker.smarthome.models.DeviceState
import com.mjrinker.smarthome.models.Room
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import okhttp3.*
import java.io.IOException

class SmartHomeAPI(
    host: String = "127.0.0.1",
    port: Int = 80,
    secure: Boolean = false
) {
    private val TAG = "SmartHomeAPI"

    private val client = OkHttpClient()
    private val baseUrl = "http${if (secure) "s" else ""}://$host:$port"

    private var url = baseUrl
    private var method = "get"
    private var json = ""
    private var callback: Any = object: Callback {
        override fun onFailure(call: Call, e: IOException) { println(e) }
        override fun onResponse(call: Call, response: Response) { println(response.body()?.string()) }
    }

    fun getRooms(rooms: ArrayList<Room>) : SmartHomeAPI {
        val path = "/rooms"
        url = "$baseUrl$path"
        method = "get"
        callback = object: Callback {
            override fun onFailure(call: Call, e: IOException) { println(e) }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseBody = response.body()
                    val responseBodyString = responseBody?.string()
                    println("$TAG->getRooms: $responseBodyString")
                    val roomsResponse = JsonPath.parse(responseBodyString)?.read<ArrayList<Room>>("$.rooms")
                    if (roomsResponse != null) {
                        for (room in roomsResponse) {
                            rooms.add(room)
                        }
                    }

                    rooms.add(
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
        return this
    }

    fun getDeviceState(deviceNames: ArrayList<String?>, deviceStates: ArrayList<DeviceState>) : SmartHomeAPI {
        val path = "/devices/state"
        url = "$baseUrl$path"
        method = "post"

        json = Gson().toJson(deviceNames)
        Log.d(TAG, json)

        callback = object: Callback {
            override fun onFailure(call: Call, e: IOException) { println(e) }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseBody = response.body()
                    val responseBodyString = responseBody?.string()
                    if (responseBodyString != null) {
                        println("$TAG->getDeviceState: response size: ${responseBodyString.length}")
                    }
                    println("$TAG->getDeviceState: $responseBodyString")
                    val deviceStateResponse = JsonPath.parse(responseBodyString)?.read<ArrayList<DeviceState>>("$.devices")
                    if (deviceStateResponse != null) {
                        for (device in deviceStateResponse) {
                            deviceStates.add(device)
                        }
                    }
                }
            }
        }
        return this
    }

    fun performAction(deviceName: String?, action: DeviceAction) : SmartHomeAPI {
        val path = "/devices/action"
        url = "$baseUrl$path"
        method = "post"

        json = Gson().toJson(arrayListOf(
            DeviceActionRequest(
                nickname = deviceName,
                actions = arrayListOf(action)
            )
        ))

        Log.d(TAG, json)
        return this
    }

    fun send() {
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        var requestBuilder = Request.Builder()
            .url(url)
            .header("X-ApiVersion", "2")

        requestBuilder = when (method) {
            "get" -> requestBuilder.get()
            "post" -> requestBuilder.post(body)
            "put" -> requestBuilder.put(body)
            "patch" -> requestBuilder.patch(body)
            "delete" -> requestBuilder.delete(body)
            "head" -> requestBuilder.head()
            else -> requestBuilder.get()
        }

        val request = requestBuilder.build()
        val response = client.newCall(request)
        response.enqueue(callback as Callback)
    }
}
