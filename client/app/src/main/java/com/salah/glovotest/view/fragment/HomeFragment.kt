package com.salah.glovotest.view.fragment

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.salah.glovotest.R
import com.salah.glovotest.view.customview.GlovoMapView
import android.widget.TextView
import com.google.android.gms.tasks.OnSuccessListener
import com.salah.glovotest.manager.LocationManager
import com.salah.glovotest.manager.NavigationManager
import com.salah.glovotest.manager.PermissionManager
import com.salah.glovotest.model.CityModel
import com.salah.glovotest.utils.DialogUtils
import com.salah.glovotest.viewmodel.HomeViewModel
import com.salah.glovotest.manager.PermissionManager.PermissionStatus
import com.salah.glovotest.utils.AppConstants.ICONS_MAP_ZOOM


/**
 * Created by salah on 12/14/18.
 */
class HomeFragment : BaseFragment() {

    private lateinit var mapView: GlovoMapView
    private lateinit var tvCityName: TextView
    private lateinit var tvCityCode: TextView
    private lateinit var tvCityCurrency: TextView
    private lateinit var tvCityTimeZone: TextView

    private lateinit var homeViewModel: HomeViewModel

    override fun getContentLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initializeComponents(view: View, savedInstanceState: Bundle?) {
        mapView = view.findViewById(R.id.map)
        tvCityName = view.findViewById(R.id.tv_city_name)
        tvCityCode = view.findViewById(R.id.tv_city_code)
        tvCityCurrency = view.findViewById(R.id.tv_city_currency)
        tvCityTimeZone = view.findViewById(R.id.tv_city_time_zone)

        mapView.onCreate(savedInstanceState)
    }

    override fun initializeData() {
        homeViewModel = ViewModelProviders.of(activity!!).get(HomeViewModel::class.java)
        initMap()
        watchSelectedCity()
        centerMatchedCity()
    }

    private fun initMap() {
        mapView.getMapAsync { googleMap ->
            //set the google map variable on the custom class
            mapView.glovoGoogleMap = googleMap
            getCities()
            googleMap.setOnCameraMoveListener {
                val cameraPosition = mapView.glovoGoogleMap!!.cameraPosition

                //hide and show markers based on the zoom level
                if (cameraPosition.zoom < ICONS_MAP_ZOOM) {
                    mapView.show()
                } else {
                    mapView.hide()
                }
            }
            googleMap.setOnMarkerClickListener({ marker ->
                homeViewModel.setCity(marker.tag as CityModel)
                true
            })
        }
    }

    private fun getCities() {
        homeViewModel.getCities().observe(this, Observer<List<CityModel>> { cities ->
            // draw cities on map
            mapView.drawPolygons(cities!!)
            mapView.drawMarkers(cities)
            checkPermission()
        })
    }

    private fun watchSelectedCity() {
        homeViewModel.getCity().observe(this, Observer<CityModel> { city ->
            if (city != null) {
                //set city data to the panel
                tvCityName.text = city.name
                tvCityCode.text = city.code
                tvCityCurrency.text = city.currency
                tvCityTimeZone.text = city.time_zone
                //zoom to the selected city
                mapView.zoomToCity(city)
            }
        })
    }

    private fun centerMatchedCity() {
        LocationManager.instance.requestLocation(OnSuccessListener { location ->
            if (location != null) {
                val city = mapView.getMatchedCity(location.latitude, location.longitude)
                if (city != null) {
                    homeViewModel.setCity(city)
                } else {
                    showDialog(getString(R.string.select_city), getString(R.string.dialog_desc))
                }
            }
        })
    }

    private fun checkPermission() {
        homeViewModel.checkPermission().observe(this, Observer<PermissionStatus> { status ->
            // update UI
            if (status == PermissionStatus.PERMISSION_GRANTED) {
                LocationManager.instance.requestLocation(OnSuccessListener { location ->
                    centerMatchedCity()
                })
            } else if (status == PermissionStatus.PERMISSION_DENIED ||
                    status == PermissionStatus.CAN_ASK_PERMISSION || homeViewModel.getCity().value == null) {
                showDialog(getString(R.string.select_city), getString(R.string.dialog__deny_desc))
            }
        })
    }

    private fun showDialog(title: String, message: String) {
        DialogUtils.showDefaultDialog(context!!,
                title,
                message,
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    NavigationManager.instance.push(CityListFragment())
                }
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        homeViewModel.setLocationPermissionStatus(PermissionManager.getPermissionStatus(activity!!, Manifest.permission.ACCESS_FINE_LOCATION))
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun initializeActions() {

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}