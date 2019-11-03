package com.morozov.rjd.domain.interfaces

import com.morozov.rjd.mvp.models.ContactModel

interface ContactsDeleter {

    fun deleteThink(contact: ContactModel, pos: Int)
}