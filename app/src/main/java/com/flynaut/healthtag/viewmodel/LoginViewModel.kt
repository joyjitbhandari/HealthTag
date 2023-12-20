package com.flynaut.healthtag.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.model.request.UserLogin
import com.flynaut.healthtag.model.response.LoginResponse
import com.flynaut.healthtag.util.Event
import kotlinx.coroutines.launch

class LoginViewModel(private val apiService: ApiService): ViewModel() {

    private val _loginResponse: MutableLiveData<Event<LoginResponse>> = MutableLiveData()

    private val _toastMsg : MutableLiveData<Event<Any>> = MutableLiveData()
    val toastMsg : LiveData<Event<Any>>
        get() = _toastMsg

    val loginResponse: LiveData<Event<LoginResponse>>
        get() = _loginResponse

    fun login(email : String, password: String) = viewModelScope.launch {

        val response = apiService.login(UserLogin(email, password,"wlkherhlkehfd1221212121122112"))
        if(response.isSuccessful)
            _loginResponse.postValue(Event(response.body()) as Event<LoginResponse>?)
        else
            _toastMsg.postValue(Event(response.body()?.message ?: R.string.something_went_wrong))
    }
}