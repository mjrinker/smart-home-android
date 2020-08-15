package com.mjrinker.smarthome

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mjrinker.smarthome.models.DeviceAction
import com.mjrinker.smarthome.models.DeviceActionRequest
import com.mjrinker.smarthome.models.Room
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.typeOf

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

    fun getRooms() : SmartHomeAPI {
        val path = "/rooms"
        url = "$baseUrl$path"
        method = "get"
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
            .header("X-ApiVersion", "2.0.0")

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
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) { println(e) }
            override fun onResponse(call: Call, response: Response) = println(response.body()?.string())
        })
    }
}
