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

class FaqViewModel(private val apiService: ApiService) : ViewModel() {


    private val _toastMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsg: LiveData<Event<String>> get() = _toastMsg

    private val _toastMsgErrorRecent: MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsgErrorRecent: LiveData<Event<String>> get() = _toastMsgErrorRecent


    private val _getFaqResponse: MutableLiveData<FaqCategoryResponse> = MutableLiveData()
    val finalFaqResponse: LiveData<FaqCategoryResponse> get() = _getFaqResponse

    private val _getAboutUsResponse: MutableLiveData<AboutUsResponse> = MutableLiveData()
    val finalAboutUsResponse: LiveData<AboutUsResponse> get() = _getAboutUsResponse

    private val _getFaqDetailResponse: MutableLiveData<FaqDetailResponse> = MutableLiveData()
    val finalFaqDetailResponse: LiveData<FaqDetailResponse> get() = _getFaqDetailResponse

    private val _getRecentTicketResponse: MutableLiveData<AllTicketResponse> = MutableLiveData()
    val finalRecentTicketResponse: LiveData<AllTicketResponse> get() = _getRecentTicketResponse


    fun getFaqCategory() = viewModelScope.launch {
        val response = apiService.getFaqCategories()
        if (response.isSuccessful){
            _getFaqResponse.value = response.body()
            Log.d("get", "_getFaqResponse: $response")
        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "_getFaqResponse: $response")
        }
    }

    fun getFaqCategoryDetail(category_id:String) = viewModelScope.launch {
        val response = apiService.getFaqCategoriesDetails(category_id)
        if (response.isSuccessful){
            _getFaqDetailResponse.value = response.body()
            Log.d("get", "_getFaqResponse: $response")
        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "_getFaqResponse: $response")
        }
    }
    fun getFaqSearch(search:String) = viewModelScope.launch {
        val response = apiService.searchFaq(SearchFaq(search))
        if (response.isSuccessful){
            _getFaqDetailResponse.value = response.body()
            Log.d("get", "_getFaqResponse: $response")
        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "_getFaqResponse: $response")
        }
    }

    fun getRecentTicket() = viewModelScope.launch {
        val response = apiService.getRecentTicket(USER_ID)
        if (response.isSuccessful){
            _getRecentTicketResponse.value = response.body()
            Log.d("get", "_getFaqResponse: $response")
        }
        else{
            _toastMsgErrorRecent.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "_getFaqResponse: $response")
        }
    }

    fun getAboutUs() = viewModelScope.launch {
        val response = apiService.getAboutUs()
        if (response.isSuccessful){
            _getAboutUsResponse.value = response.body()
            Log.d("get", "_getFaqResponse: $response")
        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "_getFaqResponse: $response")
        }
    }
}