package com.morozov.rjd.di.data

import android.content.Context
import com.morozov.rjd.domain.implementation.ContactsLoaderImpl
import com.morozov.rjd.domain.interfaces.ContactsLoader
import com.morozov.rjd.domain.interfaces.ContactsSaver
import dagger.Module
import dagger.Provides

@Module
class DataModule(private val context: Context) {

    @Provides
    fun contactsLoader(): ContactsLoader = ContactsLoaderImpl(context)

    @Provides
    fun contactSaver(): ContactsSaver = ContactsLoaderImpl(context)
}