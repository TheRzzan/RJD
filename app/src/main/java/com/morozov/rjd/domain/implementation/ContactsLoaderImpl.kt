package com.morozov.rjd.domain.implementation

import com.morozov.rjd.domain.interfaces.ContactsLoader
import com.morozov.rjd.domain.interfaces.ContactsSaver
import com.morozov.rjd.mvp.models.ContactModel
import java.util.*

class ContactsLoaderImpl: ContactsLoader, ContactsSaver {

    companion object {
        var data = mutableListOf<ContactModel>()
    }

    init {
        if (data.isEmpty()) {
            val c1 = ContactModel("Jeka", "Morozov", "Jurievich",
                "+79819600697", true, "+124456343", "Gen director",
                Date()
            )

            val c2 = ContactModel("Yurii", "Morozov", "Jurievich",
                "+79819600697", true, "+124456343", "Gen director",
                Date()
            )

            val c4 = ContactModel("Ktoto", "Tamov", "Ich",
                "+79819600697", false, "+124456343", "Gen director",
                Date()
            )

            val c3 = ContactModel("Marina", "Morozov", "Jurievich",
                "+79819600697", true, "+124456343", "Gen director",
                Date()
            )

            data = mutableListOf(c1, c2, c3, c4)
        }
    }

    override fun loadContacts(): List<ContactModel> {
        return data
    }

    override fun loadContact(pos: Int): ContactModel? =
        if (pos < data.size)
            data[pos]
        else
            null

    override fun saveNew(contact: ContactModel) {
        data.add(contact)
    }

    override fun overwriteOld(contact: ContactModel, pos: Int) {
        data.removeAt(pos)
        data.add(pos, contact)
    }
}