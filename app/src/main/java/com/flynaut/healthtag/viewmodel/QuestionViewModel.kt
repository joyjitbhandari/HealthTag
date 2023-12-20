package com.flynaut.healthtag.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.response.BaseResponse
import com.flynaut.healthtag.model.response.CategoryDetailResponse
import com.flynaut.healthtag.model.response.QuestionResponse
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.Event
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class QuestionViewModel(private val apiService: ApiService): ViewModel() {

    private val _apiResponse: MutableLiveData<QuestionResponse> = MutableLiveData()

    private val _toastMsg : MutableLiveData<Event<Any>> = MutableLiveData()
    val toastMsg : LiveData<Event<Any>>
        get() = _toastMsg

    val apiResponse: LiveData<QuestionResponse>
        get() = _apiResponse

    fun getQuestions(id:String) = viewModelScope.launch {
        val response = apiService.getQuestions(id)
        if(response.isSuccessful)
            _apiResponse.value = response.body()
        else{
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                JSONObject(errorBody ?: "").getString("error")
            } catch (e: JSONException) {
                response.message() ?: "Something went wrong"
            }
            _toastMsg.postValue(Event(errorMessage))
        }
    }


}