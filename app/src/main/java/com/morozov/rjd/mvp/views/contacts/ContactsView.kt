package com.morozov.rjd.mvp.views.contacts

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.morozov.rjd.mvp.models.ContactModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ContactsView: MvpView {

    fun showButtonsAdd()
    fun hideButtonsAdd()

    fun showContacts(data: List<ContactModel>)
}