package com.salah.glovotest.manager

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


/**
 * Created by salah on 12/14/18.
 */

object PermissionManager {

    fun getPermissionStatus(activity: Activity, permission: String): PermissionStatus {
        return when {
            PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, permission)
            -> PermissionStatus.PERMISSION_GRANTED

            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            -> PermissionStatus.CAN_ASK_PERMISSION

            else -> PermissionStatus.PERMISSION_DENIED
        }
    }

    enum class PermissionStatus {
        CAN_ASK_PERMISSION,
        PERMISSION_GRANTED,
        PERMISSION_DENIED
    }
}
