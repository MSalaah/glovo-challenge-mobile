package com.salah.glovotest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by salah on 12/14/18.
 */
class CityModel : BaseModel() {

    @SerializedName("country_code")
    @Expose
    var countryCode: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("enabled")
    @Expose
    var enabled: Boolean? = null

    @SerializedName("busy")
    @Expose
    var busy: Boolean? = null

    @SerializedName("time_zone")
    @Expose
    var time_zone: String? = null

    @SerializedName("language_code")
    @Expose
    var language_code: String? = null

    @SerializedName("working_area")
    @Expose
    var working_area: Array<String>? = null

}