package com.flynaut.healthtag.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.model.request.SignupRequest
import com.flynaut.healthtag.model.response.SignUpResponse
import com.flynaut.healthtag.util.Event
import kotlinx.coroutines.launch

class SignupViewModel(private val apiService: ApiService): ViewModel() {

    private val _signupResponse: MutableLiveData<SignUpResponse> = MutableLiveData()

    private val _toastMsg : MutableLiveData<Event<Any>> = MutableLiveData()
    val toastMsg : LiveData<Event<Any>>
        get() = _toastMsg

    val signupResponse: LiveData<SignUpResponse>
        get() = _signupResponse

    fun signup(email : String, password: String) = viewModelScope.launch {
        val response = apiService.signup(SignupRequest(email, password, password))
        if(response.isSuccessful)
            _signupResponse.value = response.body()
        else if(response.code() == 422)
            _toastMsg.postValue(Event("Email address already registered"))
        else
            _toastMsg.postValue(Event(response.body()?.message.toString() ?: R.string.something_went_wrong))
    }

}