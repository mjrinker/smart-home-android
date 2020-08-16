package com.mjrinker.smarthome.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.mjrinker.smarthome.R
import com.mjrinker.smarthome.SmartHomeAPI
import com.mjrinker.smarthome.models.Room
import kotlinx.android.synthetic.main.layout_room_list_item.view.*

class RoomRecyclerAdapter(private var rooms: ArrayList<Room>, private var onRoomListener: OnRoomListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "roomRecyclerAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.layout_room_list_item,
                    parent,
                    false
                ),
            onRoomListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RoomViewHolder -> {
                holder.bind(rooms[position])
                if (position == rooms.size - 1) {
                    // remove bottom border
                    holder.bottomBorder.setBackgroundColor(Color.argb(0, 0, 0, 0))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    fun submitList(roomList: ArrayList<Room>) {
        rooms = roomList
    }

    class RoomViewHolder constructor(
        itemView: View,
        private var onRoomListener: OnRoomListener
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val TAG = "roomRecyclerAdapter"

        private val roomLabel: TextView = itemView.room_label
        private val action1: MaterialButton = itemView.action_1
        private val action2: MaterialButton = itemView.action_2
        val bottomBorder: View = itemView.border_bottom


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            onRoomListener.onRoomClick(adapterPosition)
        }

        fun bind(room: Room) {
            roomLabel.text = room.label
            action1.text = room.actions[0].action
            action2.text = room.actions[1].action

            action1.setOnClickListener {
                SmartHomeAPI("192.168.0.107", 3031).performAction(room.name, room.actions[0]).send()
            }

            action2.setOnClickListener {
                SmartHomeAPI("192.168.0.107", 3031).performAction(room.name, room.actions[1]).send()
            }
        }
    }

    interface OnRoomListener {
        fun onRoomClick(position: Int)
    }
}
