package com.morozov.rjd.mvp.presenters.editor

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.morozov.rjd.DefaultApplication
import com.morozov.rjd.domain.interfaces.ContactsLoader
import com.morozov.rjd.mvp.views.editor.EditorView
import javax.inject.Inject

@InjectViewState
class EditorPresenter: MvpPresenter<EditorView>() {

    @Inject
    lateinit var contactsLoader: ContactsLoader

    init {
        DefaultApplication.dataComponent.inject(this)
    }

    fun loadContact(pos: Int) {
        val contact = contactsLoader.loadContact(pos) ?: return

        if (contact.isFriend)
            viewState.prepareForFriend()
        else
            viewState.prepareForColleague()

        viewState.showContact(contact)
    }
}