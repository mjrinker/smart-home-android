package com.mjrinker.smarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mjrinker.smarthome.models.Room

class RoomActivity : AppCompatActivity() {

    private val TAG = "RoomActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        if (intent.hasExtra("selected_room")) {
            val room = intent.getParcelableExtra<Room>("selected_room")

            // TODO add listeners
        }
    }
}