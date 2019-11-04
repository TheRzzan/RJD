package com.morozov.rjd.ui.activities

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.Fade
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozov.rjd.R
import com.morozov.rjd.mvp.presenters.MainPresenter
import com.morozov.rjd.mvp.views.MainView
import com.morozov.rjd.ui.fragments.contacts.ContactsFragment
import com.morozov.rjd.ui.fragments.editor.EditorFragment
import com.morozov.rjd.utility.AppConstants
import com.morozov.rjd.utility.DetailsTransition
import java.util.*

class MainActivity : MvpAppCompatActivity(), MainView {

    companion object {
        const val MAX_CLICK_DURATION = 150
        var startClickTime: Long = 0
        var startClickX: Float = 0f
        var startClickY: Float = 0f
    }

    @InjectPresenter
    lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showContacts()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            when {
                ev.action == MotionEvent.ACTION_DOWN -> {
                    startClickTime = Calendar.getInstance().timeInMillis
                    startClickX = ev.rawX
                    startClickY = ev.rawY
                }
                ev.action == MotionEvent.ACTION_UP -> {
                    val clickDuration = Calendar.getInstance().timeInMillis - startClickTime

                    if (clickDuration < MAX_CLICK_DURATION && startClickX == ev.rawX && startClickY == ev.rawY) {
                        val v = currentFocus
                        if (v is EditText) {
                            val outRect = Rect()
                            v.getGlobalVisibleRect(outRect)
                            if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                                v.clearFocus()
                                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(v.windowToken, 0)
                            }
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
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

    override fun showEditor(image: ImageView?, position: Int) {
        val fragment = EditorFragment()

        val bundle = Bundle()
        bundle.putInt(AppConstants.CONTACT_POSITION, position)

        fragment.arguments = bundle
        fragment.mActivityPresenter = mPresenter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.sharedElementEnterTransition = DetailsTransition()
            fragment.enterTransition = Fade()
            fragment.sharedElementReturnTransition = DetailsTransition()
        }

        val transaction = supportFragmentManager.beginTransaction()

        if (image != null)
            transaction.addSharedElement(image, "contactImage")

        transaction.replace(R.id.contentMain, fragment)
            .addToBackStack(null)

        transaction.commit()
    }

    override fun showEditor(string: String) {
        val fragment = EditorFragment()

        val bundle = Bundle()
        bundle.putString(AppConstants.CONTACT_WHO, string)

        fragment.arguments = bundle
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
