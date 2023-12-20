package com.flynaut.healthtag.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.R

class ItemSpacingShopList(
    private val spacing: Int,
    private val bottomSpacing: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if(position == 0 || position%2 ==0){
            outRect.left = spacing
        } else{
            outRect.right = spacing
        }
        if(position == itemCount-1){
            outRect.bottom = bottomSpacing
        }
    }
}