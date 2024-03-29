package com.morozov.rjd.ui.fragments.editor

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozov.rjd.R
import com.morozov.rjd.mvp.models.ContactModel
import com.morozov.rjd.mvp.presenters.MainPresenter
import com.morozov.rjd.mvp.presenters.editor.EditorPresenter
import com.morozov.rjd.mvp.views.editor.EditorView
import com.morozov.rjd.utility.AppConstants
import kotlinx.android.synthetic.main.fragment_editor.*
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*


class EditorFragment: MvpAppCompatFragment(), EditorView {

    /*
    * Moxy presenters
    *
    * */
    @InjectPresenter
    lateinit var mPresenter: EditorPresenter
    lateinit var mActivityPresenter: MainPresenter

    private var isDateSelected = MutableLiveData<Boolean>()

    private var mContactModel = ContactModel("", "", "",
        "", true, "", "",
        Date()
    )

    /*
    * Calendar
    *
    * */
    var calendar = Calendar.getInstance()
    val calendarListener = DatePickerDialog.OnDateSetListener{ datePicker: DatePicker, year: Int, month: Int, day: Int ->
        textDayMonthYear.text = "$day/${month + 1}/$year"

        val dayMtYrFormat = SimpleDateFormat("dd/MM/yyyy")
        val now = dayMtYrFormat.parse("$day/${month + 1}/$year")

        mContactModel.birthday = now

        isDateSelected.value = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_editor, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardView.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, 0)
        }

        buttonCross.setOnClickListener {
            activity?.onBackPressed()
        }

        val bundle = this.arguments ?: return

        when (val pos = bundle.getInt(AppConstants.CONTACT_POSITION, AppConstants.ANY_SELECTED_INT)) {
            AppConstants.ANY_SELECTED_INT -> {
                buttonCheck.setOnClickListener {
                    mContactModel.saveInDevice = checkBoxSave.isChecked
                    mPresenter.saveNew(mContactModel)
                    mActivityPresenter.showContacts()
                }
                when (bundle.getString(AppConstants.CONTACT_WHO, AppConstants.ANY_SELECTED)) {
                    AppConstants.FRIEND -> prepareForFriend()
                    AppConstants.COLLEAGUE -> prepareForColleague()
                    AppConstants.ANY_SELECTED -> mActivityPresenter.showContacts()
                }
            }
            else -> {
                buttonCheck.setOnClickListener {
                    mContactModel.saveInDevice = checkBoxSave.isChecked
                    mPresenter.overwriteOld(mContactModel, pos)
                    mActivityPresenter.showContacts()
                }
                mPresenter.loadContact(pos)
            }
        }

        buttonLoadContact.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(i, 1)
        }

        checkBoxSave.setOnCheckedChangeListener { _, _ -> verifyIsReadyToSave()}

        isDateSelected.observeForever {
            verifyIsReadyToSave()
        }

        addTextWather(editName, object : OneShotTask() {
            override fun run() {
                verifyIsReadyToSave()
                mContactModel.name = str
            }
        })
        addTextWather(editSurname, object : OneShotTask() {
            override fun run() {
                verifyIsReadyToSave()
                mContactModel.surname = str
            }
        })
        addTextWather(editFamily, object : OneShotTask() {
            override fun run() {
                verifyIsReadyToSave()
                mContactModel.family = str
            }
        })
        addTextWather(editPhone, object : OneShotTask() {
            override fun run() {
                verifyIsReadyToSave()
                mContactModel.phoneNum = str
            }
        })
        addTextWather(editPosition, object : OneShotTask() {
            override fun run() {
                verifyIsReadyToSave()
                mContactModel.position = str
            }
        })
        addTextWather(editWorkPhone, object : OneShotTask() {
            override fun run() {
                verifyIsReadyToSave()
                mContactModel.workPhone = str
            }
        })

        relativeDayMonthYear.setOnClickListener {
            showCalendar()
        }
    }

    /*
    * Helper functions
    *
    * */
    private fun verifyIsReadyToSave() {
        when(isReadyToSave()) {
            true -> buttonCheck.visibility = View.VISIBLE
            false -> buttonCheck.visibility = View.INVISIBLE
        }
    }

    private fun isReadyToSave(): Boolean {
        return if (relativeDayMonthYear.visibility == View.VISIBLE) {
            editName.text.isNotEmpty() && editFamily.text.isNotEmpty() &&
                    editPhone.text.isNotEmpty() && isDateSelected.value == true
        } else {
            editName.text.isNotEmpty() && editFamily.text.isNotEmpty() &&
                    editPhone.text.isNotEmpty() && editPosition.text.isNotEmpty() &&
                    editWorkPhone.text.isNotEmpty()
        }
    }

    private fun addTextWather(edit: EditText, task: OneShotTask) {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                task.str = s.toString()
                task.run()
            }
        })
    }

    private fun showCalendar() {
        val dialog = DatePickerDialog(
            context, calendarListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        dialog.show()
    }

    /*
    * EditorView implementation
    *
    * */
    override fun prepareForFriend() {
        textHeader.text = "Добавить друга"

        editPosition.visibility = View.GONE
        editWorkPhone.visibility = View.GONE
        relativeDayMonthYear.visibility = View.VISIBLE

        mContactModel.isFriend = true
    }

    override fun prepareForColleague() {
        textHeader.text = "Добавить коллегу"

        editPosition.visibility = View.VISIBLE
        editWorkPhone.visibility = View.VISIBLE
        relativeDayMonthYear.visibility = View.GONE

        mContactModel.isFriend = false
    }

    override fun showContact(contactModel: ContactModel) {
        checkBoxSave.isChecked = contactModel.saveInDevice

        mContactModel.photo = contactModel.photo

        if (contactModel.photo != null) {
            val imageUri = contactModel.photo
            val imageStream = activity?.contentResolver?.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            imageCard.setImageBitmap(Bitmap.createBitmap(selectedImage))
            imageCamera.visibility = View.GONE
            imageStream?.close()
        } else
            imageCamera.visibility = View.VISIBLE

        editName.setText(contactModel.name)
        editFamily.setText(contactModel.family)
        editSurname.setText(contactModel.surname)
        editPhone.setText(contactModel.phoneNum)

        if (contactModel.isFriend) {
            val dayMtYrFormat = SimpleDateFormat("dd/MM/yyyy")
            textDayMonthYear.text = dayMtYrFormat.format(contactModel.birthday)
            isDateSelected.value = true
        } else {
            editPosition.setText(contactModel.position)
            editWorkPhone.setText(contactModel.workPhone)
        }

        buttonCheck.visibility = View.INVISIBLE
    }

    internal abstract inner class OneShotTask : Runnable {
        var str: String = ""
    }

    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)

        if (reqCode == 1 && resultCode == RESULT_OK) {
            val contactUri = data?.data
            val cursor = context?.contentResolver?.query(contactUri, null, null, null, null)

            if (cursor != null && cursor.moveToFirst()) {
                val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val number = cursor.getString(numberIndex)

                val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val name = cursor.getString(nameIndex)

                /*val familyIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME)
                val family = cursor.getString(familyIndex)

                val surnameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME)
                val surname = cursor.getString(surnameIndex)*/

                editPhone.setText(number)
                editName.setText(name)
                editFamily.setText("")
                editSurname.setText("")

                checkBoxSave.isChecked = true
            }

            cursor?.close()
        } else if (reqCode == 0 && resultCode == RESULT_OK) {
            try {
                val imageUri = data?.data
                val imageStream = activity?.contentResolver?.openInputStream(imageUri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                imageCard.setImageBitmap(Bitmap.createBitmap(selectedImage))
                verifyIsReadyToSave()
                mContactModel.photo = imageUri
                imageCamera.visibility = View.GONE
                imageStream?.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        }
    }
}