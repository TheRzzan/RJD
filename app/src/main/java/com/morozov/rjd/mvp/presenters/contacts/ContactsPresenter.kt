package com.morozov.rjd.mvp.presenters.contacts

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.morozov.rjd.DefaultApplication
import com.morozov.rjd.domain.interfaces.ContactsDeleter
import com.morozov.rjd.domain.interfaces.ContactsLoader
import com.morozov.rjd.domain.interfaces.ContactsSaver
import com.morozov.rjd.mvp.models.ContactModel
import com.morozov.rjd.mvp.views.contacts.ContactsView
import javax.inject.Inject

@InjectViewState
class ContactsPresenter: MvpPresenter<ContactsView>() {

    @Inject
    lateinit var contactsLoader: ContactsLoader

    @Inject
    lateinit var contactsSaver: ContactsSaver

    @Inject
    lateinit var contactsDeleter: ContactsDeleter

    private var clicked = false

    private var isFriend: Boolean? = null

    lateinit var tmpContactsList: MutableList<ContactModel>

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

    fun deleteThink(contactModel: ContactModel, pos: Int): MutableList<ContactModel>? {
        contactsDeleter.deleteThink(contactModel, pos)
        val tmpLst = mutableListOf<ContactModel>()
        for (item in tmpContactsList) {
            if (item.phoneNum == contactModel.phoneNum) {
                tmpLst.add(item)
            }
        }
        for (item in tmpLst) {
            tmpContactsList.remove(item)
        }
        return tmpLst
    }

    fun addThink(index: Int, contactModel: ContactModel) {
        contactsSaver.saveNew(contactModel)
        tmpContactsList.add(index, contactModel)
    }

    fun addAll(data: MutableList<ContactModel>) {
        for (item in data) {
            contactsSaver.saveNew(item)
            tmpContactsList.add(item)
        }
    }

    fun loadData(b: Boolean? = null) {
        isFriend = b

        val contacts = contactsLoader.loadContacts()
        tmpContactsList = contacts.toMutableList()
        when (b) {
            true -> {
                val tmpList = mutableListOf<ContactModel>()

                for (item in contacts) {
                    if (item.isFriend)
                        tmpList.add(item)
                }
                tmpContactsList = tmpList
                viewState.showContacts(tmpContactsList)
            }
            false -> {
                val tmpList = mutableListOf<ContactModel>()

                for (item in contacts) {
                    if (!item.isFriend)
                        tmpList.add(item)
                }

                tmpContactsList = tmpList
                viewState.showContacts(tmpContactsList)
            }
            else -> viewState.showContacts(tmpContactsList)
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