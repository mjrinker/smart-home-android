package com.mjrinker.smarthome.util

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import kotlin.math.ceil
import kotlin.math.floor

class ColorHelper {
    companion object {
        fun getInverseColor(colorInt: Int): Int {
            return Color.rgb(255 - Color.red(colorInt), 255 - Color.green(colorInt), 255 - Color.blue(colorInt))
        }

        fun getResColorValue(context: Context, resId: Int): Int {
            return ContextCompat.getColor(context, resId)
        }

        fun hex2Digits(hexd: String): String {
            return if (hexd.length == 1) {
                "0$hexd"
            } else hexd
        }

        fun argbToHex(a: Int, r: Int, g: Int, b: Int): String {
            val alpha = hex2Digits(Integer.toHexString(a))
            val red = hex2Digits(Integer.toHexString(r))
            val green = hex2Digits(Integer.toHexString(g))
            val blue = hex2Digits(Integer.toHexString(b))
            return alpha + red + green + blue
        }

        fun parseColor(colorString: String): Int {
            var color = colorString.toLong(16)
            if (colorString.length == 6) {
                // Set the alpha value
                color = color or -0x1000000
            } else require(colorString.length == 8) { "Unknown color" }
            return color.toInt()
        }

        fun blendColors(percent: Double, colors: ArrayList<Int>, format: String = "int"): Any {
            val left = 0.coerceAtLeast(floor(percent * (colors.size - 1)).toInt())
            val right = (colors.size - 1).coerceAtMost(ceil(percent * (colors.size - 1)).toInt())
            val colorLeft = colors[left]
            val colorRight = colors[right]

            val leftR = (colorLeft shr 16 and 0xff).toFloat()
            val leftG = (colorLeft shr 8 and 0xff).toFloat()
            val leftB = (colorLeft and 0xff).toFloat()
            val leftA = (colorLeft shr 24 and 0xff).toFloat()

            val rightR = (colorRight shr 16 and 0xff).toFloat()
            val rightG = (colorRight shr 8 and 0xff).toFloat()
            val rightB = (colorRight and 0xff).toFloat()
            val rightA = (colorRight shr 24 and 0xff).toFloat()

            val step = 1.0 / (colors.size - 1)
            val percentRight = (percent - left * step) / step
            val percentLeft = 1.0 - percentRight

            val red = (leftR * percentLeft + rightR * percentRight).toInt()
            val green = (leftG * percentLeft + rightG * percentRight).toInt()
            val blue = (leftB * percentLeft + rightB * percentRight).toInt()
            val alpha = (leftA * percentLeft + rightA * percentRight).toInt()

            val hexColor = argbToHex(alpha, red, green, blue)

            if (format == "0xARGB") {
                return hexColor
            } else if (format == "0xRGB") {
                return hexColor.substring(2)
            }

            return parseColor(hexColor)
        }
    }
}