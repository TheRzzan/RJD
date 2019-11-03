package com.morozov.rjd.ui.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

    private val spinnerStr = listOf("Все", "Друзья", "Коллеги")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_contacts_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter1 = ArrayAdapter<String>(activity, R.layout.custom_spinner_item)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter1.clear()
        adapter1.addAll(spinnerStr)

        spinner.adapter = adapter1
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {

                    }

                    1 -> {

                    }

                    2 -> {

                    }
                }
            }
        }

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

        buttonFriend.visibility = View.VISIBLE
        buttonColleague.visibility = View.VISIBLE

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

        buttonFriend.visibility = View.GONE
        buttonColleague.visibility = View.GONE
    }
}