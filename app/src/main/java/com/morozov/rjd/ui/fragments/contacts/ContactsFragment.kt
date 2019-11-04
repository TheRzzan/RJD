package com.morozov.rjd.ui.fragments.contacts

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozov.rjd.R
import com.morozov.rjd.mvp.models.ContactModel
import com.morozov.rjd.mvp.presenters.MainPresenter
import com.morozov.rjd.mvp.presenters.contacts.ContactsPresenter
import com.morozov.rjd.mvp.views.contacts.ContactsView
import com.morozov.rjd.ui.adapters.contacts.ContactsAdapter
import com.morozov.rjd.ui.adapters.listeners.OnItemClickListener
import com.morozov.rjd.utility.AppConstants
import com.morozov.rjd.utility.ItemTouchHelperClass
import kotlinx.android.synthetic.main.fragment_contacts_list.*


class ContactsFragment: MvpAppCompatFragment(), ContactsView {

    /*
    * Moxy presenters
    *
    * */
    @InjectPresenter
    lateinit var mPresenter: ContactsPresenter
    lateinit var mActivityPresenter: MainPresenter

    lateinit var adapter: ContactsAdapter
    lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_contacts_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textAll.setOnClickListener {
            context?.resources?.getColor(R.color.colorPrimary)?.let { it1 -> textAll.setTextColor(it1) }
            context?.resources?.getColor(R.color.secondary_text)?.let { it1 -> textFriends.setTextColor(it1) }
            context?.resources?.getColor(R.color.secondary_text)?.let { it1 -> textColleagues.setTextColor(it1) }
            mPresenter.loadData()
        }

        textFriends.setOnClickListener {
            context?.resources?.getColor(R.color.colorPrimary)?.let { it1 -> textFriends.setTextColor(it1) }
            context?.resources?.getColor(R.color.secondary_text)?.let { it1 -> textAll.setTextColor(it1) }
            context?.resources?.getColor(R.color.secondary_text)?.let { it1 -> textColleagues.setTextColor(it1) }
            mPresenter.loadData(true)
        }

        textColleagues.setOnClickListener {
            context?.resources?.getColor(R.color.colorPrimary)?.let { it1 -> textColleagues.setTextColor(it1) }
            context?.resources?.getColor(R.color.secondary_text)?.let { it1 -> textAll.setTextColor(it1) }
            context?.resources?.getColor(R.color.secondary_text)?.let { it1 -> textFriends.setTextColor(it1) }
            mPresenter.loadData(false)
        }

        buttonAdd.setOnClickListener {
            mPresenter.buttonAddClicked()
        }

        buttonFriend.setOnClickListener {
            mActivityPresenter.showEditor(AppConstants.FRIEND)
        }

        buttonColleague.setOnClickListener {
            mActivityPresenter.showEditor(AppConstants.COLLEAGUE)
        }

        adapter = ContactsAdapter(object : OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                if (mPresenter.clicked)
                    mPresenter.buttonAddClicked()
                
                if (view != null)
                    mActivityPresenter.showEditor(view as ImageView, mPresenter.getGeneralPosition(position))
                else
                    mActivityPresenter.showEditor(null, mPresenter.getGeneralPosition(position))
            }
        }, mPresenter, relativeContacts)
        recyclerContacts.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context)
//        linearLayoutManager.reverseLayout = true
//        linearLayoutManager.stackFromEnd = true
        recyclerContacts.layoutManager = linearLayoutManager

        val callback = ItemTouchHelperClass(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerContacts)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPresenter.loadData()
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

    override fun showContacts(data: List<ContactModel>) {
        if (data.isEmpty())
            textEmpty.visibility = View.VISIBLE
        else
            textEmpty.visibility = View.GONE
        adapter.setData(data)
        adapter.notifyDataSetChanged()
    }
}