package com.mjrinker.smarthome.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.mjrinker.smarthome.R
import com.mjrinker.smarthome.models.DeviceAction
import com.mjrinker.smarthome.models.DeviceActionRequest
import com.mjrinker.smarthome.models.Room
import kotlinx.android.synthetic.main.layout_room_list_item.view.*
import okhttp3.*
import java.io.IOException

class RoomRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "roomRecyclerAdapter"

    private var rooms: List<Room> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.layout_room_list_item,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RoomViewHolder -> {
                holder.bind(rooms[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    fun submitList(roomList: List<Room>) {
        rooms = roomList
    }

    class RoomViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {
        private val TAG = "roomRecyclerAdapter"

        private val client = OkHttpClient()

        private val roomLabel: TextView = itemView.room_label
        private val action1: MaterialButton = itemView.action_1
        private val action2: MaterialButton = itemView.action_2

        fun bind(room: Room) {
            roomLabel.text = room.label
            action1.text = room.actions[0].action
            action2.text = room.actions[1].action

            action1.setOnClickListener {
                sendPerformActionRequest("http://192.168.0.107:3030/device/action", room.names, room.actions[0])
            }

            action2.setOnClickListener {
                sendPerformActionRequest("http://192.168.0.107:3030/device/action", room.names, room.actions[1])
            }
        }

        private fun sendPerformActionRequest(url: String, deviceNames: ArrayList<String>, action: DeviceAction) {
            val json = Gson().toJson(deviceNames.map { it ->
                DeviceActionRequest(
                    nickname = it,
                    actions = arrayListOf(action)
                )
            })

            Log.d(TAG, json)
            val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
            val request = Request.Builder()
                .url(url)
                .header("X-ApiVersion", "1.0.0")
                .post(body)
                .build()

            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) { println(e) }
                override fun onResponse(call: Call, response: Response) = println(response.body()?.string())
            })
        }
    }
}
