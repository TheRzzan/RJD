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

    private var isFriend: Boolean? = null

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
        isFriend = b

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

    fun getGeneralPosition(pos: Int): Int {
        return when (isFriend) {
            true -> {
                val contacts = contactsLoader.loadContacts()

                var i = -1
                var j = -1
                while (j != pos) {
                    if (contacts[i + 1].isFriend)
                        j++

                    i++

                    if (i > contacts.size)
                        return -1
                }

                return i
            }
            false -> {
                val contacts = contactsLoader.loadContacts()

                var i = -1
                var j = -1
                while (j != pos) {
                    if (!contacts[i + 1].isFriend)
                        j++

                    i++

                    if (i > contacts.size)
                        return -1
                }

                return i
            }
            else -> {
                pos
            }
        }
    }
}