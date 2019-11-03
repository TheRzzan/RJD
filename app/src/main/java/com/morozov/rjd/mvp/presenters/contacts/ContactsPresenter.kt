package com.morozov.rjd.mvp.presenters.contacts

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.morozov.rjd.DefaultApplication
import com.morozov.rjd.domain.interfaces.ContactsLoader
import com.morozov.rjd.mvp.models.ContactModel
import com.morozov.rjd.mvp.views.contacts.ContactsView
import javax.inject.Inject

@InjectViewState
class ContactsPresenter: MvpPresenter<ContactsView>() {

    @Inject
    lateinit var contactsLoader: ContactsLoader

    private var clicked = false

    init {
        DefaultApplication.dataComponent.inject(this)
    }

    fun buttonAddClicked() {
        clicked = !clicked

        if (clicked) {
            viewState.showButtonsAdd()
        } else {
            viewState.hideButtonsAdd()
        }
    }

    fun loadData(b: Boolean? = null) {
        val contacts = contactsLoader.loadContacts()

        when (b) {
            true -> {
                val tmpList = mutableListOf<ContactModel>()

                for (item in contacts) {
                    if (item.isFriend)
                        tmpList.add(item)
                }

                viewState.showContacts(tmpList)
            }
            false -> {
                val tmpList = mutableListOf<ContactModel>()

                for (item in contacts) {
                    if (!item.isFriend)
                        tmpList.add(item)
                }

                viewState.showContacts(tmpList)
            }
            else -> viewState.showContacts(contacts)
        }
    }
}