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
import com.flynaut.healthtag.util.SavedData
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class MyOrderViewModel(private val apiService: ApiService) : ViewModel() {


    private val _toastMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsg: LiveData<Event<String>> get() = _toastMsg

    private val _cancelOrderResponse: MutableLiveData<CancelOrderResponse> = MutableLiveData()
    val cancelOrderResponse: LiveData<CancelOrderResponse> get() = _cancelOrderResponse

    private val _getOrderResponse: MutableLiveData<OrderDetailResponse> = MutableLiveData()
    val finalGetOrderResponse: LiveData<OrderDetailResponse> get() = _getOrderResponse


    fun getOrdersFilterDetails(month: String,year:String,type:String) = viewModelScope.launch {
        val response = apiService.getMyOrdersFilter(SavedData.PREF_ID,type,month,year)
        if (response.isSuccessful){
            _getOrderResponse.value = response.body()
            Log.d("get", "getCardDetails: $response")
        }
        else{
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                JSONObject(errorBody ?: "").getString("error")
            } catch (e: JSONException) {
                response.message() ?: "Something went wrong"
            }
            _toastMsg.postValue(Event(errorMessage))
//            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "getCardDetails: $response")
        }
    }

    fun cancelOrder(orderId : String) = viewModelScope.launch {
        val response = apiService.cancelOrder(SavedData.PREF_ID, orderId)
        if (response.isSuccessful){
            _cancelOrderResponse.value = response.body()
            Log.d("get", "getCardDetails: $response")
        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "getCardDetails: $response")
        }
    }

}