package com.flynaut.healthtag.util

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.CartAdapter
import com.flynaut.healthtag.viewmodel.CartViewModel

class SwipeToDeleteProduct(
    private val adapter: CartAdapter,
    private val viewModel: CartViewModel
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val product = adapter.getItem(position)

        val alertDialogBuilder = AlertDialog.Builder(viewHolder.itemView.context)
        alertDialogBuilder.setTitle("Delete product")
        alertDialogBuilder.setMessage("Are you sure you want to delete this product?")
        alertDialogBuilder.setPositiveButton("Delete") { dialog, _ ->
            viewModel.deleteCartProduct(product._id)
            Toast.makeText(viewHolder.itemView.context, "Product removed successfully", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            adapter.notifyItemChanged(position)
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    )
    {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val icon: Drawable = ContextCompat.getDrawable(itemView.context, R.drawable.white_bin_sm)!!

            val itemHeight = itemView.bottom - itemView.top
            val isSwipingLeft = dX < 0
            val isSwipingRight = dX > 0

            val swipeBackgroundLeft: ColorDrawable = ColorDrawable(ContextCompat.getColor(itemView.context, R.color.red))
            val swipeBackgroundRight: ColorDrawable = ColorDrawable(ContextCompat.getColor(itemView.context, R.color.red))

            val iconMargin = (itemHeight - icon.intrinsicHeight) / 2
            val iconTop = itemView.top + (itemHeight - icon.intrinsicHeight) / 2
            val iconBottom = iconTop + icon.intrinsicHeight

            if (isSwipingRight) {
                val iconLeft = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + icon.intrinsicWidth

                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                swipeBackgroundRight.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
                swipeBackgroundRight.draw(c)
                icon.draw(c)
            }

            else if (isSwipingLeft) {
                val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight = itemView.right - iconMargin

                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                swipeBackgroundLeft.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                swipeBackgroundLeft.draw(c)
                icon.draw(c)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}