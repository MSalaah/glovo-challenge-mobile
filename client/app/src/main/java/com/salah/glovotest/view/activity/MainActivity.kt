package com.salah.glovotest.view.activity

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.salah.glovotest.R
import com.salah.glovotest.view.fragment.HomeFragment
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Window
import android.view.WindowManager
import com.salah.glovotest.manager.LocationManager
import com.salah.glovotest.manager.NavigationManager


class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeManagers()

        NavigationManager.instance.updateFragment(HomeFragment())
        requestPermission()
    }

    private fun initializeManagers() {
        NavigationManager.instance.initializeFragmentManager(this)
        LocationManager.instance.initializeLocationManager(this)
    }


    fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permission is granted
            LocationManager.instance.requestLocation(null)
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                NavigationManager.instance.currentFragment!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}
