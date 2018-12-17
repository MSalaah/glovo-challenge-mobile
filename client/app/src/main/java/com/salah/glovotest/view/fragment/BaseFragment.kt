package com.salah.glovotest.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by salah on 12/14/18.
 */
abstract class BaseFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(getContentLayout(), container, false)
        initializeComponents(fragmentView, savedInstanceState)
        initializeData()
        initializeActions()
        return fragmentView
    }

    protected abstract fun getContentLayout(): Int

    protected abstract fun initializeComponents(view: View, savedInstanceState: Bundle?)

    protected abstract fun initializeData()

    protected abstract fun initializeActions()
}