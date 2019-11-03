package com.morozov.rjd.domain.interfaces

import com.morozov.rjd.mvp.models.ContactModel

interface ContactsLoader {

    fun loadContacts(): List<ContactModel>
}