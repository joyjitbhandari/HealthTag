package com.flynaut.healthtag.network

import com.flynaut.healthtag.util.PrefsManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
//        val token = TokenManager.getToken()
        val token = PrefsManager.get().getString(PrefsManager.PREF_API_TOKEN,"")

        val request = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return try {
            chain.proceed(request)
        } catch (e: Exception) {
            // Handle the exception here (e.g., log, show an error message)
            throw e
        }
    }
}
