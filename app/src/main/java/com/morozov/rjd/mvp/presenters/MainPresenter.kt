package com.morozov.rjd.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.morozov.rjd.mvp.views.MainView

@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {

    fun showContacts(){
        viewState.showContacts()
    }

    fun showEditor(position: Int) {
        viewState.showEditor(position)
    }
    fun showEditor(string: String) {
        viewState.showEditor(string)
    }
}