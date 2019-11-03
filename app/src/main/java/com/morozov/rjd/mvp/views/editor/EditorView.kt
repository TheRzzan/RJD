package com.morozov.rjd.mvp.views.editor

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.morozov.rjd.mvp.models.ContactModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface EditorView: MvpView {

    fun prepareForFriend()
    fun prepareForColleague()

    fun showContact(contactModel: ContactModel)
}