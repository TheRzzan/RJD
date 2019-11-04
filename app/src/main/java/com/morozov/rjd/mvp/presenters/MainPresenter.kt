package com.morozov.rjd.mvp.presenters

import android.view.View
import android.widget.ImageView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.morozov.rjd.mvp.views.MainView

@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {

    fun showContacts(){
        viewState.showContacts()
    }

    fun showEditor(image: ImageView?, position: Int) {
        viewState.showEditor(image, position)
    }
    fun showEditor(string: String) {
        viewState.showEditor(string)
    }
}