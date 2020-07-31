package com.mjrinker.smarthome.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecorator(
    private val topPadding: Int = 0,
    private val rightPadding: Int? = null,
    private val bottomPadding: Int? = null,
    private val leftPadding: Int? = null
):  RecyclerView.ItemDecoration() {

    private val TAG = "SpacingItemDecorator"

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = topPadding ?: 0
        outRect.right = rightPadding ?: topPadding ?: 0
        outRect.bottom = bottomPadding ?: topPadding ?: 0
        outRect.left = leftPadding ?: rightPadding ?: topPadding ?: 0
    }
}