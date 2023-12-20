package com.flynaut.healthtag.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.R

class ItemSpacingHomeCards(
    private val spacing: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        /*val itemCount = state.itemCount*/

        //for 1st
        if (position == 0) {
            outRect.top = 50
            outRect.left = spacing
        }
        //for 2nd
        if (position == 1) {
            outRect.top = 50
            outRect.right = spacing
        }
        //for 3rd
        if (position == 2) {
            outRect.left = spacing
            outRect.bottom = 50
        }
        //for 4th
        if (position == 3) {
            outRect.right = spacing
            outRect.bottom = 50
        }
    }
}