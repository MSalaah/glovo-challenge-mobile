package com.salah.glovotest.network

import com.salah.glovotest.model.CityModel
import com.salah.glovotest.model.CountryModel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by salah on 12/14/18.
 */
interface IGlovoApi {

    @GET("countries")
    fun getCountries(): Call<List<CountryModel>>

    @GET("cities")
    fun getCities(): Call<List<CityModel>>

    @GET("cities/{code}")
    fun getCityDetails(@Path("code") code: String): Call<CityModel>

}