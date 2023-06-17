package com.mjrinker.smarthome.adapters

import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.mjrinker.smarthome.R
import com.mjrinker.smarthome.SmartHomeAPI
import com.mjrinker.smarthome.models.DeviceState
import com.mjrinker.smarthome.models.Room
import com.mjrinker.smarthome.models.ToggleButton
import com.mjrinker.smarthome.util.Colors
import kotlinx.android.synthetic.main.layout_room_list_item.view.*

class RoomRecyclerAdapter(private var rooms: ArrayList<Room>, private var onRoomListener: OnRoomListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "roomRecyclerAdapter"

    private lateinit var viewHolder: RoomViewHolder

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
                viewHolder = holder
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

    private fun updateRoomStatus(room: Room) {
        if (this::viewHolder.isInitialized) {
            if (DeviceState.roomIsOn(room, viewHolder.deviceStates)) {
                viewHolder.actionToggleButton.toggle(viewHolder.action2)
            } else {
                viewHolder.actionToggleButton.toggle(viewHolder.action1)
            }
        }
    }

    class RoomViewHolder constructor(
        itemView: View,
        private var onRoomListener: OnRoomListener
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val TAG = "roomRecyclerAdapter"

        private var nightMode = itemView.context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        private val COLORS = Colors(itemView.context)
        private val roomLabel: TextView = itemView.room_label
        val action1: MaterialButton = itemView.action_1
        val action2: MaterialButton = itemView.action_2
        private val actionButtons : ArrayList<MaterialButton> =  arrayListOf(action1, action2)
        val actionToggleButton = ToggleButton(
                actionButtons,
                if (nightMode) COLORS.colorDarkDefaultBackground else COLORS.colorDefaultBackground,
                COLORS.colorPrimary,
                COLORS.buttonTextColorInverse
        )
        val bottomBorder: View = itemView.border_bottom

        val deviceStates: ArrayList<DeviceState> = arrayListOf()

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

            val apiConnection = SmartHomeAPI("192.168.0.107", 3030)

            action1.setOnClickListener {
                actionToggleButton.toggle(action1)
                apiConnection.performAction(room.name, room.actions[0]).send()
            }

            action2.setOnClickListener {
                actionToggleButton.toggle(action2)
                apiConnection.performAction(room.name, room.actions[1]).send()
            }
        }
    }

    interface OnRoomListener {
        fun onRoomClick(position: Int)
    }
}
