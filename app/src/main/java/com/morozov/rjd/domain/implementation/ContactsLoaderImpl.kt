package com.morozov.rjd.domain.implementation

import com.morozov.rjd.domain.interfaces.ContactsLoader
import com.morozov.rjd.mvp.models.ContactModel
import java.util.*

class ContactsLoaderImpl: ContactsLoader {

    override fun loadContacts(): List<ContactModel> {
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

        return listOf(c1, c2, c3, c4)
    }
}