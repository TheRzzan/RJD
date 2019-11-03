package com.morozov.rjd.domain.interfaces

import com.morozov.rjd.mvp.models.ContactModel

interface ContactsSaver {

    fun saveNew(contact: ContactModel)
    fun overwriteOld(contact: ContactModel, pos: Int)
}