package com.flynaut.healthtag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.request.AddTicketRequest
import com.flynaut.healthtag.model.response.*
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.Event
import com.flynaut.healthtag.util.SavedData.USER_ID
import kotlinx.coroutines.launch

class AllTicketsViewModel(private val apiService: ApiService) : ViewModel() {


    private val _toastMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsg: LiveData<Event<String>> get() = _toastMsg

   private val _addTicketResponse: MutableLiveData<AddTicketResponse> = MutableLiveData()
    val addTicketResponse: LiveData<AddTicketResponse> get() = _addTicketResponse


    private val _getTicketDetailsResponse: MutableLiveData<AllTicketResponse> = MutableLiveData()
    val finalTicketDetailsResponse: LiveData<AllTicketResponse> get() = _getTicketDetailsResponse


    fun getAllTicketsDetail(status: String) = viewModelScope.launch {

        val response = apiService.getAllTicketFilter(USER_ID,status)
        if (response.isSuccessful){
            _getTicketDetailsResponse.value = response.body()
            Log.d("get", "getAllTicketsDetail: $response")
        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "getAllTicketsDetail: $response")
        }
    }
    fun getAllTicketsDetail() = viewModelScope.launch {

        val response = apiService.getTicketCategory(USER_ID)
        if (response.isSuccessful){
            _getTicketDetailsResponse.value = response.body()
            Log.d("get", "getAllTicketsDetail: $response")
        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "getAllTicketsDetail: $response")
        }
    }

    fun addTicket(model: AddTicketRequest)  = viewModelScope.launch {
        try{
            val response = apiService.addTicket(USER_ID,model)
            if (response.isSuccessful)
                _addTicketResponse.value = response.body()
            else
                _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
        }catch (e:Exception){
            e.printStackTrace()
            _toastMsg.postValue(Event("Connection timeout"))
        }
    }

}