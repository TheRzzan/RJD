package com.morozov.rjd.domain.implementation

import android.content.Context
import com.morozov.rjd.domain.interfaces.ContactsDeleter
import com.morozov.rjd.domain.interfaces.ContactsLoader
import com.morozov.rjd.domain.interfaces.ContactsSaver
import com.morozov.rjd.mvp.models.ContactModel
import com.morozov.rjd.utility.ContactsDBHelper

class ContactsLoaderImpl(private val context: Context): ContactsLoader, ContactsSaver, ContactsDeleter {

    companion object {
        var data = mutableListOf<ContactModel>()
    }

    init {
        if (data.isEmpty()) {
            val dbHelper = ContactsDBHelper(context)

            val count = dbHelper.getCount()
            var i = 0

            while (i < count) {
                dbHelper.getItemAt(i)?.let { data.add(it) }
                i++
            }
        }
    }

    override fun loadContacts(): MutableList<ContactModel> {
        return data
    }

    override fun loadContact(pos: Int): ContactModel? =
        if (pos < data.size)
            data[pos]
        else
            null

    override fun saveNew(contact: ContactModel) {
        data.add(contact)
        val dbHelper = ContactsDBHelper(context)
        dbHelper.addContact(contact)
    }

    override fun overwriteOld(contact: ContactModel, pos: Int) {
        data.removeAt(pos)
        data.add(pos, contact)
        val dbHelper = ContactsDBHelper(context)
        dbHelper.removeItemWithPhone(contact.phoneNum)
        dbHelper.addContact(contact)
    }

    override fun deleteThink(contact: ContactModel, pos: Int) {
        val dbHelper = ContactsDBHelper(context)
        dbHelper.removeItemWithPhone(contact.phoneNum)

        val dataTmp = mutableListOf<ContactModel>()

        for (item in data) {
            if (item.phoneNum != contact.phoneNum)
                dataTmp.add(item)
        }

        data = dataTmp
    }
}