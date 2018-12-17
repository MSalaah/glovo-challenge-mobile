package com.salah.glovotest.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.salah.glovotest.model.CityModel
import com.salah.glovotest.model.CountryModel
import com.salah.glovotest.network.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by salah on 12/15/18.
 */
class CityListViewModel : ViewModel() {

    private lateinit var countriesWithCities: MutableLiveData<List<CountryModel>>

    private val selectedCity = MutableLiveData<CityModel>()


    fun getCountries(cities: List<CityModel>): LiveData<List<CountryModel>> {
        if (!::countriesWithCities.isInitialized) {
            countriesWithCities = MutableLiveData()
            loadCountries(cities)
        }
        return countriesWithCities
    }

    private fun loadCountries(cities: List<CityModel>) {
        ApiUtils.getGlovoApi().getCountries().enqueue(object : Callback<List<CountryModel>> {
            override fun onResponse(call: Call<List<CountryModel>>, response: Response<List<CountryModel>>) {
                if (response.isSuccessful) {
                    val countries = response.body()
                    appendCitiesToCountry(cities, countries!!)
                    countriesWithCities.value = countries
                } else {
                    val statusCode = response.code()
                    // handle request errors depending on status code
                }
            }

            override fun onFailure(call: Call<List<CountryModel>>, t: Throwable) {
                print(t.message)
            }
        })
    }

    private fun appendCitiesToCountry(cities: List<CityModel>, countries: List<CountryModel>) {
        for (i in 0 until countries.size) {
            val country = countries[i]
            val citiesArrayList = arrayListOf<CityModel>()
            for (j in 0 until cities.size) {
                val city = cities[j]
                if (city.countryCode.equals(country.code)) {
                    citiesArrayList.add(city)
                }
                country.cities = citiesArrayList
            }
        }
    }

    fun getSelectedCity(): MutableLiveData<CityModel> {
        return selectedCity
    }

    fun setSelectedCity(article: CityModel) {
        selectedCity.value = article
    }
}