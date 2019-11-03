package com.morozov.rjd.ui.adapters.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import com.morozov.rjd.R
import com.morozov.rjd.mvp.models.ContactModel
import com.morozov.rjd.ui.adapters.ListAdapter
import com.morozov.rjd.ui.adapters.listeners.OnItemClickListener

class ContactsAdapter(private val listener: OnItemClickListener): ListAdapter<ContactModel, ContactsViewHolder>() {

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): ContactsViewHolder =
        ContactsViewHolder(
            LayoutInflater.from(container.context).inflate(
                R.layout.item_recycler,
                container,
                false
            )
        )

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.populate(data()[position], position, listener)
    }
}