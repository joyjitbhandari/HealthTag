package com.flynaut.healthtag.util

import com.flynaut.healthtag.model.UserProfileDataSaved
import com.google.gson.Gson

object SavedData {
    var USER_ID: String = ""
//    val userProfile: UserProfileDataSaved = Gson().fromJson(
//        PrefsManager.get().getString(PrefsManager.PREF_PROFILE, ""),
//        UserProfileDataSaved::class.java
//    )

    var PREF_ID: String =""
    var PREF_CREATE_CUSTOMER_KEY: String =""
    fun loadUserId() {
       val  userProfile = Gson().fromJson(
            PrefsManager.get().getString(PrefsManager.PREF_PROFILE, ""),
            UserProfileDataSaved::class.java
        )
         PREF_ID = userProfile._id
         PREF_CREATE_CUSTOMER_KEY = userProfile._paymentId
        USER_ID = PrefsManager.get().getString(PrefsManager.PREF_USER_ID, "")
    }
    var dashboardStatus: String = ""
}