package com.flynaut.healthtag.viewmodel

import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.response.CategoryDetailResponse
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.model.response.CategoryResponse
import com.flynaut.healthtag.util.Event
import kotlinx.coroutines.launch

class CategoryViewModel(private val apiService: ApiService): ViewModel() {

    private val _apiResponse: MutableLiveData<CategoryResponse> = MutableLiveData()
    private val _categoryDetailResponse: MutableLiveData<CategoryDetailResponse> = MutableLiveData()

    private val _toastMsg : MutableLiveData<Event<Any>> = MutableLiveData()
    val toastMsg : LiveData<Event<Any>>
        get() = _toastMsg

    val apiResponse: LiveData<CategoryResponse>
        get() = _apiResponse
    val categoryDetailResponse: LiveData<CategoryDetailResponse>
        get() = _categoryDetailResponse

    fun getCategory() = viewModelScope.launch {
        val response = apiService.getCategory()
        if(response.isSuccessful)
            _apiResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }
    fun getCategoryDetail(id:String) = viewModelScope.launch {
        val response = apiService.getCategoryDetail(id)
        if(response.isSuccessful)
            _categoryDetailResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }


}