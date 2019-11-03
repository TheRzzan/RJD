package com.morozov.rjd.mvp.presenters.contacts

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.morozov.rjd.mvp.views.contacts.ContactsView

@InjectViewState
class ContactsPresenter: MvpPresenter<ContactsView>() {

    private var clicked = false

    fun buttonAddCliced() {
        clicked = !clicked

        if (clicked) {
            viewState.showButtonsAdd()
        } else {
            viewState.hideButtonsAdd()
        }
    }
}