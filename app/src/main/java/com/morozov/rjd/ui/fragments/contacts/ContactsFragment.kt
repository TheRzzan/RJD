package com.morozov.rjd.ui.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.morozov.rjd.R
import com.morozov.rjd.mvp.presenters.MainPresenter
import com.morozov.rjd.mvp.presenters.contacts.ContactsPresenter
import com.morozov.rjd.mvp.views.contacts.ContactsView

class ContactsFragment: MvpAppCompatFragment(), ContactsView {

    /*
    * Moxy presenters
    *
    * */
    lateinit var mPresenter: ContactsPresenter
    lateinit var mActivityPresenter: MainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_contacts_list, container, false)
}