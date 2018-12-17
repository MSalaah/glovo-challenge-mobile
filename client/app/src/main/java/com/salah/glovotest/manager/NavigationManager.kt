package com.salah.glovotest.manager

import android.support.v4.app.FragmentTransaction
import com.salah.glovotest.R
import com.salah.glovotest.view.activity.MainActivity
import com.salah.glovotest.view.fragment.BaseFragment

/**
 * Created by salah on 12/14/18.
 */

class NavigationManager {

    var currentFragment: BaseFragment? = null
    private var mainActivity: MainActivity? = null

    fun initializeFragmentManager(homeActivity: MainActivity) {
        this.mainActivity = homeActivity
    }

    fun updateFragment(mCurrentFragment: BaseFragment?) {
        if (mCurrentFragment != null) {
            this.currentFragment = mCurrentFragment
            val fragmentManager = mainActivity!!.supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.main_frame, mCurrentFragment).addToBackStack(null).commit()
        }
    }

    fun push(nextFragment: BaseFragment) {
        val fragmentManager = mainActivity!!.supportFragmentManager
        fragmentManager.beginTransaction()
                .hide(currentFragment!!)
                .add(R.id.main_frame, nextFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    companion object {
        private var navigationManager: NavigationManager? = null
        val instance: NavigationManager
            get() {
                if (navigationManager == null)
                    navigationManager = NavigationManager()
                return navigationManager!!
            }
    }
}
