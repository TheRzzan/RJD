package com.morozov.rjd.di.data

import com.morozov.rjd.di.AppComponent
import com.morozov.rjd.mvp.presenters.contacts.ContactsPresenter
import dagger.Component

@Component(modules = arrayOf(DataModule::class))
interface DataComponent: AppComponent {

    fun inject(presenter: ContactsPresenter)
}