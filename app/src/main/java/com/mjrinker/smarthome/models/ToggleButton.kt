package com.mjrinker.smarthome.models

import com.google.android.material.button.MaterialButton

class ToggleButton(
        val toggleButtons: ArrayList<MaterialButton>,
        val colorOff: Int,
        val colorOn: Int,
        val colorOnText: Int
) {
    fun toggle(toggleOnButton: MaterialButton) {
        toggleOnButton.setBackgroundColor(colorOn)
        toggleOnButton.setTextColor(colorOnText)
        toggleOnButton.strokeWidth = 0

        for (toggleButton in toggleButtons) {
            if (toggleButton != toggleOnButton) {
                toggleButton.setBackgroundColor(colorOff)
                toggleButton.setTextColor(colorOn)
                toggleButton.strokeWidth = 1
            }
        }
    }
}