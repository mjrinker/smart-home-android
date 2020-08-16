package com.mjrinker.smarthome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.mjrinker.smarthome.adapters.RoomRecyclerAdapter
import com.mjrinker.smarthome.models.Room
import kotlinx.android.synthetic.main.activity_rooms_list.*

class RoomsListActivity : AppCompatActivity(), RoomRecyclerAdapter.OnRoomListener {

    private val TAG = "RoomsListActivity"

    private var rooms : ArrayList<Room>? = arrayListOf()
    private lateinit var roomAdapter: RoomRecyclerAdapter
    private var onRoomListener = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms_list)

        initRecyclerView()
        addDataSet()

        setSupportActionBar(findViewById(R.id.rooms_toolbar))
        title = "Rooms"
    }

    private fun addDataSet() {
        rooms = DataSource().loadRooms(roomAdapter)
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@RoomsListActivity)
            roomAdapter = RoomRecyclerAdapter(rooms!!, onRoomListener)
            adapter = roomAdapter
        }
    }

    override fun onRoomClick(position: Int) {
        Log.d(TAG, "onRoomClick: clicked $position")

        intent = Intent(this, RoomActivity::class.java)
        intent.putExtra("selected_room", rooms!![position])
        startActivity(intent)
    }
}