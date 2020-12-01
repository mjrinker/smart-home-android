package com.mjrinker.smarthome

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar
import com.google.android.material.button.MaterialButton
import com.mjrinker.smarthome.models.*
import com.mjrinker.smarthome.util.ColorHelper
import com.mjrinker.smarthome.util.Colors
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import com.triggertrap.seekarc.SeekArc
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlin.math.roundToInt


class RoomActivity : AppCompatActivity() {

    private val TAG = "RoomActivity"
    private val COLOR_MODE_ENABLED = 1
    private val COLOR_MODE_DISABLED = 0

    // UI components
    private lateinit var title : TextView
    private lateinit var backArrowContainer : RelativeLayout
    private lateinit var backArrow : ImageButton
    private lateinit var action1 : MaterialButton
    private lateinit var action2 : MaterialButton
    private lateinit var actionButtons : ArrayList<MaterialButton>
    private lateinit var actionToggleButton : ToggleButton
    private lateinit var brightnessControl : SeekArc
    private lateinit var brightnessProgressText : TextView
    private lateinit var temperatureControl : CrystalSeekbar
    private lateinit var colorControl : CrystalSeekbar
    private lateinit var temperatureModeToggle : MaterialButton
    private lateinit var colorModeToggle : MaterialButton
    private lateinit var lightModeButtons : ArrayList<MaterialButton>
    private lateinit var lightModeToggleButton : ToggleButton

    // Variables
    private var room : Room? = null
    private var brightnessProgress : Int = 1
    private var temperatureProgress : Number = 0
    private var colorProgress : Number = 0
    private var mode = COLOR_MODE_DISABLED
    private lateinit var COLORS : Colors
    private var nightMode : Boolean = false
    private var currentLightColor: Int = -1
    private val deviceStates: ArrayList<DeviceState> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        COLORS = Colors(this)
        nightMode = this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        if (intent.hasExtra("selected_room")) {
            room = intent.getParcelableExtra<Room>("selected_room")
            if (room != null) {
                setViews()
                setRoomProperties()
                updateLightControls()

                val clickables : ArrayList<View> = arrayListOf(
                        action1,
                        action2,
                        temperatureModeToggle,
                        colorModeToggle,
                        backArrow
                )

                for (clickable in clickables) {
                    clickable.setOnClickListener {
                        onClick(it)
                    }
                }

                brightnessControl.setOnSeekArcChangeListener(object : OnSeekArcChangeListener {
                    override fun onStartTrackingTouch(seekArc: SeekArc) {}
                    override fun onStopTrackingTouch(seekArc: SeekArc) {
                        changeBrightness()
                    }

                    override fun onProgressChanged(seekArc: SeekArc, progress: Int, fromUser: Boolean) {
                        brightnessProgress = progress
                        brightnessProgressText.text = brightnessProgress.toString()
                    }
                })

                temperatureControl.setOnSeekbarFinalValueListener { value ->
                    temperatureProgress = value
                    changeColorTemperature()
                }

                colorControl.setOnSeekbarFinalValueListener { value ->
                    colorProgress = value
                    changeColor()
                }
            }
        }
    }

    private fun changeBrightness() {
        performAction(DeviceAction(
                "brightness",
                brightnessProgress
        ))
    }

    private fun changeColor() {
        performAction(DeviceAction(
                "color",
                ColorHelper.blendColors(colorProgress.toDouble() / 100, LightState(0, "000000", 0).colorInts, "0xRGB")
        ))
        setCurrentLightColor()
        brightnessControl.progressColor = currentLightColor
    }

    private fun changeColorTemperature() {
        performAction(DeviceAction(
                "temperature",
                temperatureProgress
        ))
        setCurrentLightColor()
        brightnessControl.progressColor = currentLightColor
    }

    private fun changeColorValue(button: MaterialButton) {
        when (button) {
            temperatureModeToggle -> {
                changeColorTemperature()
            }
            colorModeToggle -> {
                changeColor()
            }
        }
    }

    private fun disableColorMode() {
        temperatureControl.visibility = View.VISIBLE
        colorControl.visibility = View.GONE
        mode = COLOR_MODE_DISABLED
        updateLightControls()
    }

    private fun enableColorMode() {
        colorControl.visibility = View.VISIBLE
        temperatureControl.visibility = View.GONE
        mode = COLOR_MODE_ENABLED
        updateLightControls()
    }

    private fun onClick(view: View) {
        when (view) {
            backArrow -> {
                finish()
            }
            in lightModeButtons -> {
                toggleColorMode(view as MaterialButton)
                lightModeToggleButton.toggle(view)
                if (DeviceState.roomIsOn(room!!, deviceStates)) {
                    changeColorValue(view)
                }
            }
            in actionButtons -> {
                actionToggleButton.toggle(view as MaterialButton)
                val action = room!!.actions[actionButtons.indexOf(view)]
                performAction(
                        action,
                        object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                println(e)
                            }

                            override fun onResponse(call: Call, response: Response) {
                                response.use {
                                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                                    val responseBody = response.body()
                                    val responseBodyString = responseBody?.string()
                                    println("$TAG->getDeviceState: $responseBodyString")
                                    val deviceStateResponse = JsonPath.parse(responseBodyString)?.read<ArrayList<DeviceState>>("$.devices")
                                    if (deviceStateResponse != null) {
                                        for (device in deviceStateResponse) {
                                            deviceStates.add(device)
                                        }
                                    }
                                    updateLightControls()
                                }
                            }
                        }
                )
            }
        }
    }

    private fun performAction(action: DeviceAction, callback: Any? = null) {
        brightnessControl.progressColor = if (action.action == "on") currentLightColor else COLORS.colorOff
        val apiConnection = SmartHomeAPI("192.168.0.107", 3031)

        if (callback != null) {
            apiConnection.callback = callback
        }

        apiConnection.performAction(
                room!!.name,
                action
        ).send()
    }

    private fun setCurrentLightColor() {
        if (mode == COLOR_MODE_DISABLED) {
            currentLightColor = temperatureControl.leftThumbColor
        } else if (mode == COLOR_MODE_ENABLED) {
            currentLightColor = colorControl.leftThumbColor
        }
    }

    private fun setRoomProperties() {
        title.text = room?.label
        action1.text = room?.actions?.get(0)?.action
        action2.text = room?.actions?.get(1)?.action
    }

    private fun setViews() {
        title = findViewById(R.id.toolbar_title)
        backArrowContainer = findViewById(R.id.back_arrow_container)
        backArrow = findViewById(R.id.toolbar_back_arrow)
        action1 = findViewById(R.id.action_1)
        action2 = findViewById(R.id.action_2)
        actionButtons = arrayListOf(action1, action2)
        actionToggleButton = ToggleButton(
                actionButtons,
                if (nightMode) COLORS.colorDarkDefaultBackground else COLORS.colorDefaultBackground,
                COLORS.colorPrimary,
                COLORS.buttonTextColorInverse
        )
        brightnessControl = findViewById(R.id.brightness_control)
        brightnessProgressText = findViewById(R.id.brightness_progress)
        temperatureControl = findViewById(R.id.temperature_control)
        colorControl = findViewById(R.id.color_control)
        temperatureModeToggle = findViewById(R.id.temperature_mode_toggle)
        colorModeToggle = findViewById(R.id.color_mode_toggle)
        lightModeButtons = arrayListOf(temperatureModeToggle, colorModeToggle)
        lightModeToggleButton = ToggleButton(
                lightModeButtons,
                if (nightMode) COLORS.colorDarkDefaultBackground else COLORS.colorDefaultBackground,
                COLORS.colorPrimary,
                COLORS.buttonTextColorInverse
        )
    }

    private fun toggleColorMode(button: MaterialButton) {
        if (button == temperatureModeToggle) {
            disableColorMode()
        } else if (button == colorModeToggle) {
            enableColorMode()
        }
    }

    private fun updateLightControls(forceGetStates: Boolean = true) {
        if (forceGetStates) {
            DeviceState.getDeviceStates(room!!, deviceStates)
        }
        while (deviceStates.size == 0) {}

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val brightnesses = deviceStates
                    .filter { it.light_state != null && it.light_state.brightness > 0 }
                    .map { if (it.light_state != null) it.light_state.brightness else 0 }
            val temperatures = deviceStates
                    .filter { it.light_state != null && it.light_state.color_temp > 0 }
                    .map { if (it.light_state != null) it.light_state.color_temp else 0 }
            val colorPercents : List<Float> = deviceStates
                    .filter { it.light_state != null }
                    .map { if (it.light_state != null) it.light_state.colorPercent() else 0f }

            val brightnessAverage =
                    if (brightnesses.isNotEmpty()) brightnesses.average() else brightnessProgress.toDouble()
            val temperatureAverage =
                    if (temperatures.isNotEmpty()) temperatures.average() else temperatureProgress.toDouble()
            val colorPercentAverage =
                    if (colorPercents.isNotEmpty()) colorPercents.average() else colorProgress.toDouble()

            brightnessProgress = 1.coerceAtLeast(brightnessAverage.roundToInt())
            temperatureProgress = 0.coerceAtLeast(temperatureAverage.roundToInt())
            colorProgress = 0.coerceAtLeast(colorPercentAverage.roundToInt())

            brightnessControl.progress = brightnessProgress
            brightnessProgressText.text = brightnessProgress.toString()
            temperatureControl.setMinStartValue(temperatureProgress.toFloat()).apply()
            colorControl.setMinStartValue(colorProgress.toFloat()).apply()

            setCurrentLightColor()
            val progressColor: Int
            lateinit var currentActionButton: MaterialButton
            if (DeviceState.roomIsOn(room!!, deviceStates)) {
                progressColor = currentLightColor
                currentActionButton = action2
            } else {
                progressColor = COLORS.colorOff
                currentActionButton = action1
            }

            if (brightnessControl.progressColor != progressColor) {
                brightnessControl.progressColor = progressColor
            }

            if ((currentActionButton.background as? ColorDrawable)?.color != COLORS.colorPrimary) {
                actionToggleButton.toggle(currentActionButton)
            }
        }, 200)
    }
}