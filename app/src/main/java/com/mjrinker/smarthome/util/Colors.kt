package com.mjrinker.smarthome.util

import android.content.Context
import com.google.android.material.button.MaterialButton
import com.mjrinker.smarthome.R

class Colors(context: Context) {
    private val defaultMaterialButton = MaterialButton(context, null, R.style.MaterialOutlinedButton)

    val buttonTextColorInverse = ColorHelper.getInverseColor(defaultMaterialButton.currentTextColor)
    val colorPrimary = ColorHelper.getResColorValue(context, R.color.colorPrimary)
    val colorDefaultBackground = ColorHelper.getResColorValue(context, R.color.design_default_color_background)
    val colorDarkDefaultBackground = ColorHelper.getResColorValue(context, R.color.design_dark_default_color_background)
    val colorOff = ColorHelper.getResColorValue(context, R.color.colorOff)
}