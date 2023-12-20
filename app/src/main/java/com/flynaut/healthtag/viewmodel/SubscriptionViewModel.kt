package com.flynaut.healthtag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.model.response.SubscriptionDetails
import com.flynaut.healthtag.model.response.SubscriptionDetailsResponse
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.SavedData.USER_ID
import kotlinx.coroutines.launch

class SubscriptionViewModel(private val apiService: ApiService) : ViewModel() {

    private val get_all_subscription_list: MutableLiveData<List<SubscriptionDetails>> = MutableLiveData()
    val getAllSubscriptionList: LiveData<List<SubscriptionDetails>> get() = get_all_subscription_list

    private val get_subscription_api_response: MutableLiveData<SubscriptionDetailsResponse> = MutableLiveData()
    val getSubscriptionApiResponse: LiveData<SubscriptionDetailsResponse> get() = get_subscription_api_response


    fun getAllSubscriptionDetails() = viewModelScope.launch {
        val response = apiService.getAllSubscriptions(USER_ID)
        if (response.isSuccessful){
            get_all_subscription_list.value = response.body()?.data
            Log.d("get", "getAllCardDetails: $response")
        }
        else{
//            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "getAllCardDetails: $response")
        }


    }

    fun getSubscriptionDetails( subscriptionId: String) = viewModelScope.launch {
        val response = apiService.getSubscriptionDetails(USER_ID,subscriptionId)
        if (response.isSuccessful){
            get_subscription_api_response.value = response.body()
            Log.d("get", "getCardDetails: $response")
        }
        else{
//            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "getCardDetails: $response")
        }
    }
}
