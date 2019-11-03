package com.morozov.rjd.mvp.presenters.contacts

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.morozov.rjd.DefaultApplication
import com.morozov.rjd.domain.interfaces.ContactsLoader
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

    fun loadData() {
        viewState.showContacts(contactsLoader.loadContacts())
    }
}