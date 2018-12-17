package com.salah.glovotest.network

import com.salah.glovotest.utils.AppConstants
import com.salah.glovotest.utils.AppConstants.BASE_URL

/**
 * Created by salah on 12/14/18.
 */
object ApiUtils {

    fun getGlovoApi(): IGlovoApi {
        return ApiClientInstance.getClient(String.format(BASE_URL, AppConstants.IP_ADDRESS)).create(IGlovoApi::class.java)
    }
}