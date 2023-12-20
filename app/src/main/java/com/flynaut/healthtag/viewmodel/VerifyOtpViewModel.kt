package com.flynaut.healthtag.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.model.response.OtpVerifyResponse
import com.flynaut.healthtag.util.Event
import kotlinx.coroutines.launch

class VerifyOtpViewModel(private val apiService: ApiService): ViewModel() {

    private val _apiResponse: MutableLiveData<(OtpVerifyResponse)> = MutableLiveData()
    private val _toastMsg : MutableLiveData<Event<Any>> = MutableLiveData()
    val toastMsg : LiveData<Event<Any>>
        get() = _toastMsg

    val apiResponse: LiveData<OtpVerifyResponse>
        get() = _apiResponse

    fun verifyOtp(fields : Map<String, String>) = viewModelScope.launch {
        val response = apiService.verifyOtp(fields)
        if(response.isSuccessful)
            _apiResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }

}