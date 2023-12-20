package com.flynaut.healthtag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.Add_DeviceResponse
import com.flynaut.healthtag.model.request.AddDevice
import com.flynaut.healthtag.model.response.AllDeviceResponse
import com.flynaut.healthtag.model.response.DeleteDeviceResponse
import com.flynaut.healthtag.model.response.GetCartResponse
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.Event
import com.flynaut.healthtag.util.SavedData
import kotlinx.coroutines.launch

class DeviceListViewModel(private val apiService: ApiService): ViewModel() {

    private val _apiResponse: MutableLiveData<AllDeviceResponse> = MutableLiveData()
    private val _deleteDeviceResponse: MutableLiveData<DeleteDeviceResponse> = MutableLiveData()

    private val _toastMsg : MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsg : LiveData<Event<String>>
        get() = _toastMsg

    val apiResponse: LiveData<AllDeviceResponse>
        get() = _apiResponse

    val deleteResponse: LiveData<DeleteDeviceResponse>
        get() = _deleteDeviceResponse

    fun allDevice() = viewModelScope.launch {
        val response = apiService.getAllDevice(SavedData.USER_ID)
        Log.e("test", response.toString())
        if(response.isSuccessful)
            _apiResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.body()?.data ?: R.string.something_went_wrong) as Event<String>?)

    }

    fun deleteDevice(_id : String) = viewModelScope.launch {
        val response = apiService.deleteDevice(SavedData.USER_ID,_id)
        Log.e("test", response.toString())
        if(response.isSuccessful)
            _deleteDeviceResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.body()?.message ?: R.string.something_went_wrong) as Event<String>?)
    }

}