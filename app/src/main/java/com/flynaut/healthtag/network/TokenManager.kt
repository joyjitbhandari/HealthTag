package com.flynaut.healthtag.network

object TokenManager {
    private var token: String? = null

    fun setToken(token: String) {
        this.token = token
    }

    fun getToken(): String? {
        return token
    }

    fun clearToken() {
        token = null
    }
}
