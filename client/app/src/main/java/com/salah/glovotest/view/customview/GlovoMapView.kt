package com.salah.glovotest.view.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.gms.maps.*
import com.salah.glovotest.R
import com.google.maps.android.PolyUtil
import com.salah.glovotest.model.CityModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.salah.glovotest.utils.AppConstants.MAP_ZOOM
import com.google.android.gms.maps.model.Marker


/**
 * Created by salah on 12/14/18.
 */
class GlovoMapView : MapView {

    var glovoGoogleMap: GoogleMap? = null

    val polygonsOnMap = ArrayList<Polygon>()

    val markersOnMap = ArrayList<Marker>()

    var markersShown: Boolean? = true

    constructor(context: Context) : super(context) {
        initializeMapView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initializeMapView()
    }

    constructor(context: Context, attributeSet: AttributeSet, i: Int) : super(context, attributeSet, i) {
        initializeMapView()
    }

    constructor(context: Context, googleMapOptions: GoogleMapOptions) : super(context, googleMapOptions) {
        initializeMapView()
    }

    private fun initializeMapView() {
        LayoutInflater.from(context).inflate(R.layout.view_map, this, true)
        getMapAsync { googleMap ->
            googleMap.uiSettings.isTiltGesturesEnabled = false
            googleMap.uiSettings.isRotateGesturesEnabled = false
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            googleMap.isTrafficEnabled = false
        }
    }

    fun drawPolygons(cities: List<CityModel>) {
        polygonsOnMap.clear()
        for (i in 0 until cities.size) {
            val city = cities[i]

            val cityWorkingArea = city.working_area

            val cityEncodedPolygons: Array<String> = cityWorkingArea!!.map { it }.toTypedArray()

            for (j in 0 until cityEncodedPolygons.size) {

                //decode using google api
                val latLngList = PolyUtil.decode(cityEncodedPolygons[j])

                if (latLngList.isNotEmpty()) {
                    val polygonOptions = PolygonOptions()
                            .fillColor(Color.argb(50, 0, 255, 0))
                            .strokeColor(Color.BLUE)
                            .addAll(latLngList)

                    if (glovoGoogleMap != null) {
                        //add polygon to map
                        val poly = glovoGoogleMap!!.addPolygon(polygonOptions)
                        poly.tag = city
                        polygonsOnMap.add(poly)
                    }
                }
            }
        }
    }

    private fun workingAreaToPolygon(workingArea: MutableList<String>): List<LatLng> {
        var latLngList = listOf<LatLng>()
        for (j in 0 until workingArea.size) {
            latLngList = PolyUtil.decode(workingArea[j])
        }
        return latLngList
    }

    fun getMatchedCity(latitude: Double, longitude: Double): CityModel? {
        for (i in 0 until polygonsOnMap.size) {
            if (isPointInside(LatLng(latitude, longitude), polygonsOnMap[i].points)) {
                return polygonsOnMap[i].tag as CityModel
            }
        }
        return null
    }

    fun drawMarkers(cities: List<CityModel>) {
        for (i in 0 until cities.size) {
            val city = cities[i]
            val cityEncodedPolygons: MutableList<String> = city.working_area!!.map { it }.toMutableList()
            if (cityEncodedPolygons.isNotEmpty()) {
                val latLngList = PolyUtil.decode(cityEncodedPolygons[0])
                if (latLngList.isNotEmpty()) {
                    val carMarkerOption = MarkerOptions()
                            .anchor(0.0f, 1.0f)
                            .position(latLngList[0])
                    if (glovoGoogleMap != null) {
                        val marker = glovoGoogleMap!!.addMarker(carMarkerOption)
                        marker.tag = city
                        markersOnMap.add(marker)
                    }
                }
            }
        }
    }

    fun hide() {
        if (markersShown!!) {
            for (i in 0 until markersOnMap.size) {
                val marker = markersOnMap[i]
                marker.isVisible = false
            }
            markersShown = false
        }
    }

    fun show() {
        if (!markersShown!!) {
            for (i in 0 until markersOnMap.size) {
                val marker = markersOnMap[i]
                marker.isVisible = true
            }
            markersShown = true
        }
    }

    fun zoomToCity(city: CityModel) {
        val cityEncodedPolygons: MutableList<String> = city.working_area!!.map { it }.toMutableList()
        animateCamera(getCentroid(workingAreaToPolygon(cityEncodedPolygons)))
    }

    private fun animateCamera(location: LatLng) {
        if (this.glovoGoogleMap != null) {
            this.glovoGoogleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(location, MAP_ZOOM))
        }
    }

    private fun isPointInside(location: LatLng, polygon: List<LatLng>): Boolean {
        return PolyUtil.containsLocation(location.latitude, location.longitude, polygon, true)
    }

    private fun getCentroid(points: List<LatLng>): LatLng {
        val centroid = doubleArrayOf(0.0, 0.0)
        for (i in 0 until points.size) {
            centroid[0] += points[i].latitude
            centroid[1] += points[i].longitude
        }
        val totalPoints = points.size
        centroid[0] = centroid[0] / totalPoints
        centroid[1] = centroid[1] / totalPoints

        return LatLng(centroid[0], centroid[1])
    }
}