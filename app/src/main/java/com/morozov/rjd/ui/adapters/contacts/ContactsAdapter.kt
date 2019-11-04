package com.morozov.rjd.ui.adapters.contacts

import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.morozov.rjd.R
import com.morozov.rjd.mvp.models.ContactModel
import com.morozov.rjd.mvp.presenters.contacts.ContactsPresenter
import com.morozov.rjd.ui.adapters.ListAdapter
import com.morozov.rjd.ui.adapters.listeners.OnItemClickListener
import com.morozov.rjd.utility.ItemTouchHelperClass

class ContactsAdapter(private val listener: OnItemClickListener, private val mPresenter: ContactsPresenter, private val view: View): ListAdapter<ContactModel, ContactsViewHolder>(),
    ItemTouchHelperClass.ItemTouchHelperAdapter{

    lateinit var justDeletedItem: MutableList<ContactModel>
    var indexOfDeletedItem = -1

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): ContactsViewHolder =
        ContactsViewHolder(
            LayoutInflater.from(container.context).inflate(
                R.layout.item_card_recycler,
                container,
                false
            )
        )

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.populate(data()[position], position, listener)
    }

    /*
    * ItemTouchHelperAdapter implementation
    *
    * */
    override fun onItemRemoved(position: Int) {
        justDeletedItem = mPresenter.deleteThink(data()[position], position)!!
        indexOfDeletedItem = position
        notifyDataSetChanged()

        Snackbar.make(view, "Удалено", Snackbar.LENGTH_LONG)
            .setAction("Отмена") {
                mPresenter.addAll(justDeletedItem)
                notifyDataSetChanged()
            }.show()
    }
}