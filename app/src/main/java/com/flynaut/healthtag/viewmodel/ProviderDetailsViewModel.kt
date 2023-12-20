package com.flynaut.healthtag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.request.SearchFaq
import com.flynaut.healthtag.model.response.*
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.Event
import com.flynaut.healthtag.util.SavedData.USER_ID
import kotlinx.coroutines.launch

class ProviderDetailsViewModel(private val apiService: ApiService) : ViewModel() {


    private val _toastMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsg: LiveData<Event<String>> get() = _toastMsg

    private val _getProvidersResponse: MutableLiveData<List<ProviderDetails>> = MutableLiveData()
    val providersResponse: LiveData<List<ProviderDetails>> get() = _getProvidersResponse

    private val _getSingleProviderDetailsResponse: MutableLiveData<SingleProviderResponse> = MutableLiveData()
    val singleProviderResponse: LiveData<SingleProviderResponse> get() = _getSingleProviderDetailsResponse

    private val _searchProvider: MutableLiveData<ProviderSearchResponse> = MutableLiveData()
    val searchProviderResponse: LiveData<ProviderSearchResponse> get() =  _searchProvider

    fun getAllProvider() = viewModelScope.launch {

        val response = apiService.getAllProviderApi(USER_ID)
        if (response.isSuccessful){
            _getProvidersResponse.value = response.body()?.providerDetails
            Log.d("get", "getAllProvider: $response")
        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "getAllProvider: $response")
        }
    }

    fun getProviderDetails(providerId: String) = viewModelScope.launch {
        val response = apiService.getProviderApi(providerId, USER_ID)
        if (response.isSuccessful){
            _getSingleProviderDetailsResponse.value = response.body()
        }else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
        }
    }

    fun setProviderDetails(providerId: String) = viewModelScope.launch {
        val response = apiService.addProviderDetails(USER_ID,providerId)
        if (!response.isSuccessful){
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
        }
    }


    fun searchProvider(search : String) = viewModelScope.launch {
        val response = apiService.searchProvider(SearchFaq(search))
        if (response.isSuccessful){
            _searchProvider.value = response.body()
        }else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
        }
    }

}