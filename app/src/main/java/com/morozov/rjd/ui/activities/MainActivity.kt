package com.morozov.rjd.ui.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozov.rjd.R
import com.morozov.rjd.mvp.presenters.MainPresenter
import com.morozov.rjd.mvp.views.MainView
import com.morozov.rjd.ui.fragments.contacts.ContactsFragment
import com.morozov.rjd.ui.fragments.editor.EditorFragment

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showContacts()
    }

    /*
    * MainView implementation
    *
    * */
    override fun showContacts() {
        val fragment = ContactsFragment()

        fragment.mActivityPresenter = mPresenter

        clearBackStack()
        setFragment(fragment, false)
    }

    override fun showEditor() {
        val fragment = EditorFragment()

        fragment.mActivityPresenter = mPresenter

        setFragment(fragment, true)
    }

    /*
    * Helper functions
    *
    * */
    private fun setFragment(fragment: Fragment, b: Boolean = false) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.contentMain, fragment)

        if (b)
            transaction.addToBackStack(null)

        transaction.commit()
    }

    private fun clearBackStack() {
        var i = 0
        while (i < supportFragmentManager.backStackEntryCount){
            i++
            supportFragmentManager.popBackStack()
        }
    }
}
