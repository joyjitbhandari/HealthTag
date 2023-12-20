package com.flynaut.healthtag.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.model.response.SignUpResponse
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.Event
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(private val apiService: ApiService): ViewModel() {

    private val _apiResponse: MutableLiveData<SignUpResponse> = MutableLiveData()
    private val _toastMsg : MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsg : LiveData<Event<String>>
        get() = _toastMsg

    val apiResponse: LiveData<SignUpResponse>
        get() = _apiResponse

    fun forgotPassword(fields : Map<String, String>) = viewModelScope.launch {
        val response = apiService.forgotPassword(fields)
        if(response.isSuccessful)
            _apiResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: ""))
    }

}