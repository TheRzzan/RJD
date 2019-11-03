package com.morozov.rjd.di.data

import com.morozov.rjd.domain.implementation.ContactsLoaderImpl
import com.morozov.rjd.domain.interfaces.ContactsLoader
import com.morozov.rjd.domain.interfaces.ContactsSaver
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun contactsLoader(): ContactsLoader = ContactsLoaderImpl()

    @Provides
    fun contactSaver(): ContactsSaver = ContactsLoaderImpl()
}