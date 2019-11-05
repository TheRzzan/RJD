package com.morozov.rjd.utility

import android.Manifest
import android.content.ContentProviderOperation
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import com.morozov.rjd.mvp.models.ContactModel
import java.text.SimpleDateFormat

object ContactsPhoneHelper {

    fun saveNewContact(context: Context, contact: ContactModel): Boolean {
        val ops = mutableListOf<ContentProviderOperation>()

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build())

        // ------------------------------------------------------ Full name
        ops.add(ContentProviderOperation
            .newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "${contact.family} ${contact.name} ${contact.surname}")
            .build())

        // ------------------------------------------------------ Mobile Number
        ops.add(ContentProviderOperation
            .newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.phoneNum)
            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build())

        // ------------------------------------------------------ Work Number
        if (contact.workPhone != null) {
            ops.add(
                ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.workPhone)
                    .withValue(
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK
                    ).build()
            )
        }

        // ------------------------------------------------------ Organization
        if (!contact.position.equals("")) {
            ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, contact.position)
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE,
                    ContactsContract.CommonDataKinds.Organization.TYPE_WORK).build())
        }

        // ------------------------------------------------------ Birthday
        if (contact.isFriend) {
            val dayMtYrFormat = SimpleDateFormat("dd-MM-yyyy")

            ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, dayMtYrFormat.format(contact.birthday))
                .withValue(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)
                .build())
        }

        // ------------------------------------------------------ Photo
//        if (contact.photo != null) {
//            ops.add(ContentProviderOperation
//                .newInsert(ContactsContract.Data.CONTENT_URI)
//                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                .withValue(ContactsContract.Data.MIMETYPE,
//                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
//                .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO_URI, contact.photo)
//                .build())
//        }

        var isSuccess = false

        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
                return false
            }
            // Asking the Contact provider to create a new contact
            context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ArrayList(ops))
            isSuccess = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return isSuccess
    }

    fun overwriteOldContact(context: Context, contact: ContactModel) {

    }

    fun deleteContact(context: Context, contact: ContactModel) {

    }
}