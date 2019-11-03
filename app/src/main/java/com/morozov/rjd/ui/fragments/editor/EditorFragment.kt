package com.morozov.rjd.ui.fragments.editor

import android.app.DatePickerDialog
import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozov.rjd.mvp.models.ContactModel
import com.morozov.rjd.mvp.presenters.MainPresenter
import com.morozov.rjd.mvp.presenters.editor.EditorPresenter
import com.morozov.rjd.mvp.views.editor.EditorView
import com.morozov.rjd.utility.AppConstants
import kotlinx.android.synthetic.main.fragment_editor.*
import java.text.SimpleDateFormat
import android.text.Editable
import android.text.TextWatcher
import android.widget.DatePicker
import com.morozov.rjd.R
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

    private val mContactModel = ContactModel("", "", "",
        "", true, "", "",
        Date()
    )

    /*
    * Calendar
    *
    * */
    var calendar = Calendar.getInstance()
    val calendarListener = DatePickerDialog.OnDateSetListener{ datePicker: DatePicker, year: Int, month: Int, day: Int ->
        textDayMonthYear.text = "$day.${month + 1}.$year"

        val now = Date()
        now.year = year
        now.month = month + 1
        now.date = day

        isDateSelected.value = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_editor, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonCross.setOnClickListener {
            activity?.onBackPressed()
        }

        val bundle = this.arguments ?: return

        when (val pos = bundle.getInt(AppConstants.CONTACT_POSITION, AppConstants.ANY_SELECTED_INT)) {
            AppConstants.ANY_SELECTED_INT -> {
                buttonCheck.setOnClickListener {
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
                    mPresenter.overwriteOld(mContactModel, pos)
                    mActivityPresenter.showContacts()
                }
                mPresenter.loadContact(pos)
            }
        }

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
        editPosition.visibility = View.GONE
        editWorkPhone.visibility = View.GONE
        relativeDayMonthYear.visibility = View.VISIBLE

        mContactModel.isFriend = true
    }

    override fun prepareForColleague() {
        editPosition.visibility = View.VISIBLE
        editWorkPhone.visibility = View.VISIBLE
        relativeDayMonthYear.visibility = View.GONE

        mContactModel.isFriend = false
    }

    override fun showContact(contactModel: ContactModel) {
        if (contactModel.name.isNotEmpty())
            textLetter.text = contactModel.name[0].toString()

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
}