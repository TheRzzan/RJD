package com.morozov.rjd.utility

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.morozov.rjd.mvp.models.ContactModel
import java.text.SimpleDateFormat

class ContactsDBHelper(context: Context): SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "contacts_v10.db"
        private const val DATABASE_VERSION = 7

        const val TABLE_NAME = "contacts_v10"

        const val _ID = "_ID"
        const val COLUMN_NAME = "column_name"
        const val COLUMN_FAMILU = "column_family"
        const val COLUMN_SURNAME = "column_surname"
        const val COLUMN_BIRTHDAY = "column_birthday"
        const val COLUMN_PHONE_NUM = "column_phoneNum"
        const val COLUMN_IS_FRIEND = "column_isFriend"
        const val COLUMN_WORK_PHONE = "column_workPhone"
        const val COLUMN_POSITION = "column_position"
        const val COLUMN_PHOTO = "column_photo"

        private val TEXT_TYPE = " TEXT"
        private val BOOL_TYPE = " BOOLEAN"
        private val COMMA_SEP = ","

        private val SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                COLUMN_NAME +        TEXT_TYPE + COMMA_SEP +
                COLUMN_FAMILU +      TEXT_TYPE + COMMA_SEP +
                COLUMN_SURNAME +     TEXT_TYPE + COMMA_SEP +
                COLUMN_BIRTHDAY +    TEXT_TYPE + COMMA_SEP +
                COLUMN_PHONE_NUM +   TEXT_TYPE + COMMA_SEP +
                COLUMN_IS_FRIEND +   BOOL_TYPE + COMMA_SEP +
                COLUMN_WORK_PHONE +  TEXT_TYPE + COMMA_SEP +
                COLUMN_PHOTO +       TEXT_TYPE + COMMA_SEP +
                COLUMN_POSITION +    TEXT_TYPE + ")"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
    }

    fun getCount(): Int {
        val db = readableDatabase
        val projection = arrayOf(_ID)
        val c = db.query(TABLE_NAME, projection, null, null, null, null, null)
        val count = c.count
        c.close()
        return count
    }

    fun getItemAt(position: Int): ContactModel? {
        val db = readableDatabase
        val projection = arrayOf(
            _ID,
            COLUMN_NAME,
            COLUMN_FAMILU,
            COLUMN_SURNAME,
            COLUMN_BIRTHDAY,
            COLUMN_PHONE_NUM,
            COLUMN_IS_FRIEND,
            COLUMN_WORK_PHONE,
            COLUMN_PHOTO,
            COLUMN_POSITION
        )

        val c = db.query(TABLE_NAME, projection, null, null, null, null, null)
        if (c.moveToPosition(position)) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy/HH:mm:ss")

            val item = ContactModel(
                c.getString(c.getColumnIndex(COLUMN_NAME)),
                c.getString(c.getColumnIndex(COLUMN_FAMILU)),
                c.getString(c.getColumnIndex(COLUMN_SURNAME)),
                c.getString(c.getColumnIndex(COLUMN_PHONE_NUM)),
                c.getInt(c.getColumnIndex(COLUMN_IS_FRIEND)) == 1,
                c.getString(c.getColumnIndex(COLUMN_WORK_PHONE)),
                c.getString(c.getColumnIndex(COLUMN_POSITION)),
                dateFormat.parse(c.getString(c.getColumnIndex(COLUMN_BIRTHDAY))),
                when (c.getString(c.getColumnIndex(COLUMN_PHOTO)).isEmpty()) {
                    true -> null
                    false -> Uri.parse(c.getString(c.getColumnIndex(COLUMN_PHOTO)))
                }
            )
            c.close()
            return item
        }
        return null
    }

    fun removeItemWithId(id: Int) {
        val db = writableDatabase
        val whereArgs = arrayOf((id + 1).toString())
        db.delete(TABLE_NAME, "_ID=?", whereArgs)
    }

    fun removeItemWithPhone(phone: String) {
        val db = writableDatabase
        val whereArgs = arrayOf(phone)
        db.delete(TABLE_NAME, "$COLUMN_PHONE_NUM=?", whereArgs)
    }

    fun addContact(contact: ContactModel): Long {
        val db = writableDatabase
        val cv = ContentValues()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy/HH:mm:ss")

        cv.put(COLUMN_NAME, contact.name)
        cv.put(COLUMN_FAMILU, contact.family)
        cv.put(COLUMN_SURNAME, contact.surname)
        cv.put(COLUMN_BIRTHDAY, dateFormat.format(contact.birthday))
        cv.put(COLUMN_PHONE_NUM, contact.phoneNum)
        cv.put(COLUMN_IS_FRIEND, contact.isFriend)
        cv.put(COLUMN_WORK_PHONE, contact.workPhone)

        cv.put(
            COLUMN_PHOTO,
            when (contact.photo == null) {
                true -> ""
                false -> contact.photo.toString()
            }
        )

        cv.put(COLUMN_POSITION, contact.position)

        val rowId = db.insert(TABLE_NAME, null, cv)

        return rowId
    }
}