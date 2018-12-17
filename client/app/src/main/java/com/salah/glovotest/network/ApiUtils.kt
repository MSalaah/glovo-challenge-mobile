package com.salah.glovotest.network

import com.salah.glovotest.utils.AppConstants.BASE_URL

/**
 * Created by salah on 12/14/18.
 */
object ApiUtils {

    fun getGlovoApi(): IGlovoApi {
        return ApiClientInstance.getClient(BASE_URL).create(IGlovoApi::class.java)
    }
}