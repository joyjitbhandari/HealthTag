package com.flynaut.healthtag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.Add_DeviceResponse
import com.flynaut.healthtag.model.request.AddDevice
import com.flynaut.healthtag.model.request.EditDeviceRequest
import com.flynaut.healthtag.model.response.UpdateDeviceResponse
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.Event
import com.flynaut.healthtag.util.SavedData
import kotlinx.coroutines.launch

class AddDeviceViewModel(private val apiService: ApiService): ViewModel() {

    private val _apiResponse: MutableLiveData<Add_DeviceResponse> = MutableLiveData()
    private val _apiUpdateResponse: MutableLiveData<UpdateDeviceResponse> = MutableLiveData()
    private val _toastMsg : MutableLiveData<Event<String>> = MutableLiveData()

    val toastMsg : LiveData<Event<String>>
        get() = _toastMsg

    val apiResponse: LiveData<Add_DeviceResponse>
        get() = _apiResponse

    val updateDeviceResponse: LiveData<UpdateDeviceResponse>
        get() = _apiUpdateResponse

    fun addDevice(deviceID: String,name: String,protocolType: String?,serialNumber: String, RSSI: String,
                  currentTestType: String,advertisementData: String, nameCached: String, shortName: String,
                  bootID: String) = viewModelScope.launch {
        Log.e("DeID",deviceID)
        Log.e("name",name)
        Log.e("protocolType",protocolType.toString())
        Log.e("serialNumber",serialNumber)
        Log.e("RSSI",RSSI)
        Log.e("advertisementData",advertisementData)
        Log.e("nameCached",nameCached)
        Log.e("shortName",shortName)
        Log.e("bootID",bootID)
        Log.e("USER_ID",SavedData.USER_ID)
        val response = apiService.add_Device(SavedData.USER_ID,AddDevice(deviceID, name, protocolType, serialNumber, RSSI, currentTestType, advertisementData, nameCached, shortName, bootID))
        Log.e("Resoos",response.body().toString())
        if(response.isSuccessful)
            _apiResponse.value = response.body()
        else if(response.code() == 409)
            _toastMsg.postValue(Event("Device details already added for the user"))
        else if(response.code() == 500)
            _toastMsg.postValue(Event("Failed to add device details"))

        else
            _toastMsg.postValue(Event(response.body()?.message ?: R.string.something_went_wrong) as Event<String>?)
                  }
    fun updateDevice(deviceID: String,editDeviceRequest: EditDeviceRequest) = viewModelScope.launch {
        val response = apiService.updateDevice(SavedData.USER_ID,deviceID, editDeviceRequest)
        if(response.isSuccessful)
            _apiUpdateResponse.value = response.body()
           else
            _toastMsg.postValue(Event(response.body()?.message ?: R.string.something_went_wrong) as Event<String>?)
    }
}