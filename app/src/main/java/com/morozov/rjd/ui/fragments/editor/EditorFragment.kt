package com.morozov.rjd.ui.fragments.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozov.rjd.R
import com.morozov.rjd.mvp.models.ContactModel
import com.morozov.rjd.mvp.presenters.MainPresenter
import com.morozov.rjd.mvp.presenters.editor.EditorPresenter
import com.morozov.rjd.mvp.views.editor.EditorView
import com.morozov.rjd.utility.AppConstants
import kotlinx.android.synthetic.main.fragment_editor.*
import java.text.SimpleDateFormat

class EditorFragment: MvpAppCompatFragment(), EditorView {

    /*
    * Moxy presenters
    *
    * */
    @InjectPresenter
    lateinit var mPresenter: EditorPresenter
    lateinit var mActivityPresenter: MainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_editor, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonCross.setOnClickListener {
            activity?.onBackPressed()
        }

        buttonCheck.setOnClickListener {
            mActivityPresenter.showContacts()
        }

        val bundle = this.arguments ?: return

        when (val pos = bundle.getInt(AppConstants.CONTACT_POSITION, AppConstants.ANY_SELECTED_INT)) {
            AppConstants.ANY_SELECTED_INT -> {
                when (bundle.getString(AppConstants.CONTACT_WHO, AppConstants.ANY_SELECTED)) {
                    AppConstants.FRIEND -> prepareForFriend()
                    AppConstants.COLLEAGUE -> prepareForColleague()
                    AppConstants.ANY_SELECTED -> activity?.onBackPressed()
                }
            }
            else -> {
                mPresenter.loadContact(pos)
            }
        }
    }

    /*
    * EditorView implementation
    *
    * */
    override fun prepareForFriend() {
        editPosition.visibility = View.GONE
        editWorkPhone.visibility = View.GONE
        relativeDayMonthYear.visibility = View.VISIBLE
    }

    override fun prepareForColleague() {
        editPosition.visibility = View.VISIBLE
        editWorkPhone.visibility = View.VISIBLE
        relativeDayMonthYear.visibility = View.GONE
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
        } else {
            editPosition.setText(contactModel.position)
            editWorkPhone.setText(contactModel.workPhone)
        }
    }
}