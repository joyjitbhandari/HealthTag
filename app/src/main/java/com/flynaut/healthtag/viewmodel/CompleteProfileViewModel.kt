package com.flynaut.healthtag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.request.CompleteProfileRequest
import com.flynaut.healthtag.model.response.ProfileResponse
import com.flynaut.healthtag.model.response.SignUpResponse
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.Event
import com.flynaut.healthtag.util.SavedData.USER_ID
import kotlinx.coroutines.launch

class CompleteProfileViewModel(private val apiService: ApiService): ViewModel() {

    private val _toastMsg : MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsg : LiveData<Event<String>>
        get() = _toastMsg

    private val _apiResponse: MutableLiveData<ProfileResponse> = MutableLiveData()
    val apiResponse: LiveData<ProfileResponse>
        get() = _apiResponse

    private val profile_apiResponse: MutableLiveData<ProfileResponse> = MutableLiveData()
    val getProfileResponse: LiveData<ProfileResponse>
        get() = profile_apiResponse

    private val update_apiResponse: MutableLiveData<ProfileResponse> = MutableLiveData()
    val profileUpdateResponse: LiveData<ProfileResponse>
        get() = update_apiResponse

    fun completeProfile(userId:String,completeProfileRequest: CompleteProfileRequest) = viewModelScope.launch {
        val response = apiService.completeProfile(userId, completeProfileRequest)
        if(response.isSuccessful){
            _apiResponse.value = response.body()
            Log.d("get", "completeProfile: ${response.body()}")
        }
        else {
            _toastMsg.postValue(Event(response.message() ?: ""))
            Log.d("error", "completeProfile: ${response.code()},${response.errorBody()}")
        }
    }

    fun getProfile() = viewModelScope.launch {
        val response = apiService.getProfile(USER_ID)
        if(response.isSuccessful)
            profile_apiResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
    }


    fun updateProfile(completeProfileRequest: CompleteProfileRequest) = viewModelScope.launch {
        val response = apiService.updateProfile(USER_ID,completeProfileRequest)
        if(response.isSuccessful){
            update_apiResponse.value = response.body()
            update()
        }
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
    }

    private fun update() {
        getProfile()
    }

}