package com.mjrinker.smarthome

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar
import com.google.android.material.button.MaterialButton
import com.mjrinker.smarthome.models.DeviceAction
import com.mjrinker.smarthome.models.Room
import com.triggertrap.seekarc.SeekArc
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener


class RoomActivity : AppCompatActivity() {

    private val TAG = "RoomActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        if (intent.hasExtra("selected_room")) {
            val room = intent.getParcelableExtra<Room>("selected_room")

            if (room != null) {
                // TODO persist progress
                // TODO make back arrow go back
                val title = findViewById<TextView>(R.id.toolbar_title)
                val action1 = findViewById<MaterialButton>(R.id.action_1)
                val action2 = findViewById<MaterialButton>(R.id.action_2)
                val brightnessControl : SeekArc = findViewById(R.id.brightness_control)
                val colorControl = findViewById<CrystalSeekbar>(R.id.color_control)
                var brightnessProgress : Int = 0
                var colorProgress : Number = 0

                title.text = room.label
                action1.text = room.actions[0].action
                action2.text = room.actions[1].action

                action1.setOnClickListener {
                    SmartHomeAPI("192.168.0.107", 3031).performAction(room.name, room.actions[0]).send()
                }

                action2.setOnClickListener {
                    SmartHomeAPI("192.168.0.107", 3031).performAction(room.name, room.actions[1]).send()
                }

                brightnessControl.setOnSeekArcChangeListener(object : OnSeekArcChangeListener {
                    override fun onStartTrackingTouch(seekArc: SeekArc) {}

                    override fun onStopTrackingTouch(seekArc: SeekArc) {
                        SmartHomeAPI("192.168.0.107", 3031).performAction(room.name, DeviceAction("brightness", brightnessProgress)).send()
                    }

                    override fun onProgressChanged(seekArc: SeekArc, progress: Int, fromUser: Boolean) {
                        brightnessProgress = progress
                    }
                })

                colorControl.setOnSeekbarFinalValueListener { value ->
                    colorProgress = value
                    SmartHomeAPI("192.168.0.107", 3031).performAction(room.name, DeviceAction("temperature", colorProgress)).send()
                }
            }
        }
    }
}