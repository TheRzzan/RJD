package com.morozov.rjd.utility

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class ItemTouchHelperClass(private val adapter: ItemTouchHelperAdapter): ItemTouchHelper.Callback() {

    interface ItemTouchHelperAdapter {
        fun onItemRemoved(position: Int)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recycler: RecyclerView, holder: RecyclerView.ViewHolder): Int {
        val upFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END

        return makeMovementFlags(upFlags, swipeFlags)
    }

    override fun onMove(recycler: RecyclerView, holder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemRemoved(holder.getAdapterPosition())
    }
}