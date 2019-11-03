package com.morozov.rjd.ui.fragments.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.morozov.rjd.R
import com.morozov.rjd.mvp.presenters.MainPresenter
import com.morozov.rjd.mvp.presenters.editor.EditorPresenter
import com.morozov.rjd.mvp.views.editor.EditorView

class EditorFragment: MvpAppCompatFragment(), EditorView {

    /*
    * Moxy presenters
    *
    * */
    lateinit var mPresenter: EditorPresenter
    lateinit var mActivityPresenter: MainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_editor, container, false)
}