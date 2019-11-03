package com.morozov.rjd.ui.adapters

import android.support.v7.widget.RecyclerView

abstract class ListAdapter<T, VH : RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {

    private var data: List<T> = ArrayList<T>()

    protected fun data(): List<T> = data

    fun setData(data: List<T>) {
        this.data = data
    }

    override fun getItemCount(): Int = data.size
}