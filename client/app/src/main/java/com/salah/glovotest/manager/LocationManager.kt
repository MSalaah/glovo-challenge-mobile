package com.salah.glovotest.manager

import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.salah.glovotest.view.activity.MainActivity

/**
 * Created by salah on 12/15/18.
 */
class LocationManager {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var currentLocation: Location? = null
    private var mainActivity: MainActivity? = null

    fun initializeLocationManager(mainActivity: MainActivity) {
        this.mainActivity = mainActivity
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity)
    }

    fun requestLocation(onSuccessListener: OnSuccessListener<Location>?) {
        if (ContextCompat.checkSelfPermission(mainActivity!!,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            currentLocation = location
                            onSuccessListener?.onSuccess(location)
                        }
                    }
        }
    }

    companion object {
        private var locationManager: LocationManager? = null
        val instance: LocationManager
            get() {
                if (locationManager == null)
                    locationManager = LocationManager()
                return locationManager!!
            }
    }
}