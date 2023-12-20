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
import com.flynaut.healthtag.util.SavedData
import kotlinx.coroutines.launch

class AllLogsListViewModel(private val apiService: ApiService) : ViewModel() {


    private val _toastMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsg: LiveData<Event<String>> get() = _toastMsg

   private val _toastMsgError: MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsgError: LiveData<Event<String>> get() = _toastMsgError


    private val _getLogsResponse: MutableLiveData<LogsListDetailsResponse> = MutableLiveData()
    val finalLogsResponse: LiveData<LogsListDetailsResponse> get() = _getLogsResponse



    fun getLogs() = viewModelScope.launch {
        val response = apiService.getDeviceLog(SavedData.USER_ID)
        if (response.isSuccessful){
            _getLogsResponse.value = response.body()
            Log.d("get", "_getFaqResponse: $response")
        }
        else{
            _toastMsgError.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "_getFaqResponse: $response")
        }
    }

}