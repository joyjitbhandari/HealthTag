package com.flynaut.healthtag.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.response.BaseResponse
import com.flynaut.healthtag.model.response.CategoryDetailResponse
import com.flynaut.healthtag.model.response.QuestionResponse
import com.flynaut.healthtag.model.response.ResultResponse
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.Event
import kotlinx.coroutines.launch

class ResultViewModel(private val apiService: ApiService): ViewModel() {

    private val _apiResponse: MutableLiveData<ResultResponse> = MutableLiveData()

    private val _toastMsg : MutableLiveData<Event<Any>> = MutableLiveData()
    val toastMsg : LiveData<Event<Any>>
        get() = _toastMsg

    val apiResponse: LiveData<ResultResponse>
        get() = _apiResponse

    fun getResult(id:String, fields : Map<String, Int>) = viewModelScope.launch {
        val response = apiService.getResult(id, fields)
        if(response.isSuccessful)
            _apiResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }


}