package com.flynaut.healthtag.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.request.AddFamilyMemberRequest
import com.flynaut.healthtag.model.response.*
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.Event
import com.flynaut.healthtag.util.SavedData
import com.flynaut.healthtag.util.SavedData.USER_ID
import com.flynaut.healthtag.util.SavedData.PREF_ID
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class FamilyMemberViewModel(private val apiService: ApiService) : ViewModel() {

    private val add_family_member_api_response: MutableLiveData<AddFamilyMemberResponse> = MutableLiveData()
    val addFamilyMemberApiResponse: LiveData<AddFamilyMemberResponse> get() = add_family_member_api_response

    private val get_all_family_member_list: MutableLiveData<List<FamilyMember>> = MutableLiveData()
    val getAllFamilyMemberList: LiveData<List<FamilyMember>> get() = get_all_family_member_list

    private val delete_family_member_api_response: MutableLiveData<FamilyMemberResponse> = MutableLiveData()
    val deleteFamilyMemberApiResponse: LiveData<FamilyMemberResponse> get() = delete_family_member_api_response

    private val update_family_member_api_response: MutableLiveData<FamilyMemberResponse> = MutableLiveData()
    val updateFamilyMemberApiResponse: LiveData<FamilyMemberResponse> get() = update_family_member_api_response

    private val _toastMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsg: LiveData<Event<String>> get() = _toastMsg

    fun addFamilyMember(addFamilyMemberRequest: AddFamilyMemberRequest) = viewModelScope.launch {
        val response = apiService.addFamilyMember(PREF_ID,addFamilyMemberRequest)
        if (response.isSuccessful) {
            add_family_member_api_response.value = response.body()
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                JSONObject(errorBody ?: "").getString("error")
            } catch (e: JSONException) {
                response.message() ?: "Something went wrong"
            }
            _toastMsg.postValue(Event(errorMessage))
//            _toastMsg.postValue(
//                Event(response.message() ?: R.string.something_went_wrong.toString())
//            )
        }
    }


    //For fetching all member
    fun getAllFamilyMember() = viewModelScope.launch {
        val response = apiService.getAllFamilyMember(PREF_ID)
        if (response.isSuccessful){
            get_all_family_member_list.value = response.body()?.data?.familyMembers
            Log.d("get", "getAllFamilyMember: $response")
        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "getAllFamilyMember: $response")
        }


    }

    //For deleting member
    fun deleteFamilyMember(memberId: String) = viewModelScope.launch {
        val response = apiService.deleteFamilyMember(PREF_ID,memberId)
        if (response.isSuccessful){
            delete_family_member_api_response.value = response.body()
            updateList()
        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
        }
    }


    //For updating member
    fun updateFamilyMember(memberId: String,updateFamilyMemberRequest: AddFamilyMemberRequest) = viewModelScope.launch {
        val response = apiService.updateFamilyMember(USER_ID,memberId,updateFamilyMemberRequest)
        if (response.isSuccessful){
            update_family_member_api_response.value = response.body()
            updateList()
            Log.d("get", "deleteFamilyMember: $response")

        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong.toString()))
            Log.d("error", "deleteFamilyMember: $response")
        }
    }

    //after delete list item to update all list
    private fun updateList() {
        getAllFamilyMember()
    }
}