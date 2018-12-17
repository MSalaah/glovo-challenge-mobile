package com.salah.glovotest.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.salah.glovotest.manager.PermissionManager.PermissionStatus
import com.salah.glovotest.model.CityModel
import com.salah.glovotest.network.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by salah on 12/14/18.
 */
class HomeViewModel : ViewModel() {

    lateinit var cities: MutableLiveData<List<CityModel>>

    private val selectedCity = MutableLiveData<CityModel>()

    private var locationPermissionStatus = MutableLiveData<PermissionStatus>()

    fun setLocationPermissionStatus(newPermissionStatus: PermissionStatus) {
        if (locationPermissionStatus.value != newPermissionStatus) {
            locationPermissionStatus.value = newPermissionStatus
        }
    }

    fun checkPermission(): MutableLiveData<PermissionStatus> {
        return locationPermissionStatus
    }


    fun getCities(): LiveData<List<CityModel>> {
        if (!::cities.isInitialized) {
            cities = MutableLiveData()
            loadCities()
        }
        return cities
    }

    private fun loadCities() {
        ApiUtils.getGlovoApi().getCities().enqueue(object : Callback<List<CityModel>> {
            override fun onResponse(call: Call<List<CityModel>>, response: Response<List<CityModel>>) {
                if (response.isSuccessful) {
                    cities.value = response.body()
                } else {
                    val statusCode = response.code()
                    // handle request errors depending on status code
                }
            }

            override fun onFailure(call: Call<List<CityModel>>, t: Throwable) {
                print(t.message)
            }
        })
    }

    private fun loadCityDetails(cityModel: CityModel) {
        ApiUtils.getGlovoApi().getCityDetails(cityModel.code!!).enqueue(object : Callback<CityModel> {
            override fun onResponse(call: Call<CityModel>, response: Response<CityModel>) {
                if (response.isSuccessful) {
                    selectedCity.value = response.body()
                }
            }

            override fun onFailure(call: Call<CityModel>, t: Throwable) {
                print(t.message)
            }
        })
    }

    fun getCity(): MutableLiveData<CityModel> {
        return selectedCity
    }

    fun setCity(city: CityModel) {
        loadCityDetails(city)
    }
}