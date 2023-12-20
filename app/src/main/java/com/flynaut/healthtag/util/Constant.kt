package com.flynaut.healthtag.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class Constant {

    companion object {

        val IMAGE_URL = "https://dev-healthtag-devapi.flynautstaging.com/uploads/"

        fun monthsArray(): ArrayList<Months> {
            val arrayMonth = ArrayList<Months>()

            // Add "Select Month" as the first item
            val selectMonth = Months()
            selectMonth.name = "Months"
            selectMonth.monthInteger = ""
            arrayMonth.add(selectMonth)

            // Add the other months
            val monthsNames = arrayOf(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )
            for (i in 1..12) {
                val month = Months()
                month.name = monthsNames[i - 1]
                month.monthInteger = i.toString()
                arrayMonth.add(month)
            }

            return arrayMonth
        }

    }
    class Months {
        var monthInteger = ""
        var name: String? = null

        override fun toString(): String {
            return name!! // What to display in the Spinner list.
        }
    }

    object WebViewKeys {
        const val PRIVACY_POLICY_LINK = "https://dev-healthtag-webapp.flynautstaging.com/privacypolicy"
        const val RETURN_POLICY_LINK = "https://dev-healthtag-webapp.flynautstaging.com/returnpolicy"
        const val COOKIE_POLICY_LINK = "https://dev-healthtag-webapp.flynautstaging.com/cookiepolicy"
        const val TERMS_CONDITION_LINK = "https://dev-healthtag-webapp.flynautstaging.com/termsofuse"
    }

    fun check_permissions(context: Activity): Boolean {
        val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if (!hasPermissions(context, *PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(PERMISSIONS, 2)
            }
        } else {
            return true
        }
        return false
    }


    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }


    
}