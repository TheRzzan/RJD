package com.morozov.rjd.mvp.views

import android.view.View
import android.widget.ImageView
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView: MvpView {

    fun showContacts()
    fun showEditor(image: ImageView?, position: Int)
    fun showEditor(string: String)
}