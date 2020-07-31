package com.mjrinker.smarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mjrinker.smarthome.adapters.RoomRecyclerAdapter
import com.mjrinker.smarthome.util.SpacingItemDecorator
import kotlinx.android.synthetic.main.activity_rooms_list.*

class RoomsListActivity : AppCompatActivity() {

    private val TAG = "RoomsListActivity"

    private lateinit var roomAdapter: RoomRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms_list)

        initRecyclerView()
        addDataSet()
    }

    private fun addDataSet() {
        DataSource().loadRooms(roomAdapter)
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@RoomsListActivity)
            val topSpacingDecorator = SpacingItemDecorator(30, 0, 0, 0)
            addItemDecoration(topSpacingDecorator)
            roomAdapter = RoomRecyclerAdapter()
            adapter = roomAdapter
        }
    }
}