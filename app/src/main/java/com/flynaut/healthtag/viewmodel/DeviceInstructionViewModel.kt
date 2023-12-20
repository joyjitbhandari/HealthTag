package com.flynaut.healthtag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.response.*
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.Event
import com.flynaut.healthtag.util.SavedData.USER_ID
import kotlinx.coroutines.launch

class DeviceInstructionViewModel(private val apiService: ApiService) : ViewModel() {


    private val _toastMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsg: LiveData<Event<String>> get() = _toastMsg


    private val _getDeviceInstResponse: MutableLiveData<OrderDetailResponse> = MutableLiveData()
    val finalDeviceInstResponse: LiveData<OrderDetailResponse> get() = _getDeviceInstResponse

    fun getDeviceInstruction() = viewModelScope.launch {
        val response = apiService.getMyOrdersFilter(USER_ID,"Hypertension","","")
        if (response.isSuccessful){
            _getDeviceInstResponse.value = response.body()
            Log.d("get", "getCardDetails: $response")
        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "getCardDetails: $response")
        }
    }

}