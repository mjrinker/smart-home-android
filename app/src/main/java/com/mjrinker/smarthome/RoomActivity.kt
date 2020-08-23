package com.mjrinker.smarthome

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar
import com.google.android.material.button.MaterialButton
import com.mjrinker.smarthome.models.DeviceAction
import com.mjrinker.smarthome.models.DeviceState
import com.mjrinker.smarthome.models.Room
import com.triggertrap.seekarc.SeekArc
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener
import kotlin.math.roundToInt


class RoomActivity : AppCompatActivity() {

    private val TAG = "RoomActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        if (intent.hasExtra("selected_room")) {
            val room = intent.getParcelableExtra<Room>("selected_room")

            if (room != null) {
                val title : TextView = findViewById(R.id.toolbar_title)
                val backArrow : ImageButton = findViewById(R.id.toolbar_back_arrow)
                val action1 : MaterialButton = findViewById(R.id.action_1)
                val action2 : MaterialButton = findViewById(R.id.action_2)
                val brightnessControl : SeekArc = findViewById(R.id.brightness_control)
                val brightnessProgressText : TextView = findViewById(R.id.brightness_progress)
                val colorControl : CrystalSeekbar = findViewById(R.id.color_control)
                var brightnessProgress : Int = 0
                var colorProgress : Number = 0


                fun updateLightControls() {
                    val deviceStates: ArrayList<DeviceState> = arrayListOf()
                    SmartHomeAPI("192.168.0.107", 3030).getDeviceState(
                        arrayListOf(room.name),
                        deviceStates
                    ).send()

                    while (deviceStates.size == 0) {
                        Log.d(TAG, "onCreate: null")
                    }

                    brightnessProgress = deviceStates.map { it -> it.light_state.brightness }.average().roundToInt()
                    colorProgress = deviceStates.map { it -> it.light_state.temperature }.average().roundToInt()

                    brightnessControl.progress = brightnessProgress
                    brightnessProgressText.text = 0.coerceAtLeast(brightnessProgress).toString()
                    colorControl.setMinStartValue(colorProgress.toFloat()).apply()
                }

                updateLightControls()

                title.text = room.label
                action1.text = room.actions[0].action
                action2.text = room.actions[1].action

                action1.setOnClickListener {
                    SmartHomeAPI("192.168.0.107", 3030).performAction(room.name, room.actions[0]).send()
                    updateLightControls()
                }

                action2.setOnClickListener {
                    SmartHomeAPI("192.168.0.107", 3030).performAction(room.name, room.actions[1]).send()
                    updateLightControls()
                }

                backArrow.setOnClickListener {
                    finish()
                }

                brightnessControl.setOnSeekArcChangeListener(object : OnSeekArcChangeListener {
                    override fun onStartTrackingTouch(seekArc: SeekArc) {}

                    override fun onStopTrackingTouch(seekArc: SeekArc) {
                        SmartHomeAPI("192.168.0.107", 3030).performAction(room.name, DeviceAction("brightness", brightnessProgress)).send()
                    }

                    override fun onProgressChanged(seekArc: SeekArc, progress: Int, fromUser: Boolean) {
                        brightnessProgress = progress
                        brightnessProgressText.text = brightnessProgress.toString()
                    }
                })

                colorControl.setOnSeekbarFinalValueListener { value ->
                    colorProgress = value
                    SmartHomeAPI("192.168.0.107", 3030).performAction(room.name, DeviceAction("temperature", colorProgress)).send()
                }
            }
        }
    }
}