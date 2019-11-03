package com.morozov.rjd.ui.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozov.rjd.R
import com.morozov.rjd.mvp.presenters.MainPresenter
import com.morozov.rjd.mvp.presenters.contacts.ContactsPresenter
import com.morozov.rjd.mvp.views.contacts.ContactsView
import kotlinx.android.synthetic.main.fragment_contacts_list.*

class ContactsFragment: MvpAppCompatFragment(), ContactsView {

    /*
    * Moxy presenters
    *
    * */
    @InjectPresenter
    lateinit var mPresenter: ContactsPresenter
    lateinit var mActivityPresenter: MainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_contacts_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAdd.setOnClickListener {
            mPresenter.buttonAddCliced()
        }
    }

    /*
    * ContactsView implementation
    *
    * */
    override fun showButtonsAdd() {
        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        scaleUp.fillAfter = true
        scaleUp.isFillEnabled = true

        val animRotRight = AnimationUtils.loadAnimation(context, R.anim.rotate_center_right)
        animRotRight.fillAfter = true
        animRotRight.isFillEnabled = true

        buttonFriend.startAnimation(scaleUp)
        buttonColleague.startAnimation(scaleUp)
        buttonAdd.startAnimation(animRotRight)
    }

    override fun hideButtonsAdd() {
        val scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down)
        scaleDown.fillAfter = true
        scaleDown.isFillEnabled = true

        val animRotLeft = AnimationUtils.loadAnimation(context, R.anim.rotate_center_left)
        animRotLeft.fillAfter = true
        animRotLeft.isFillEnabled = true

        buttonFriend.startAnimation(scaleDown)
        buttonColleague.startAnimation(scaleDown)
        buttonAdd.startAnimation(animRotLeft)
    }
}