package com.flynaut.healthtag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.request.CreateCardRequest
import com.flynaut.healthtag.model.request.CreateCustomerRequest
import com.flynaut.healthtag.model.request.DeleteCardRequest
import com.flynaut.healthtag.model.request.GetCardRequest
import com.flynaut.healthtag.model.response.*
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.Event
import com.flynaut.healthtag.util.PrefsManager
import com.flynaut.healthtag.util.SavedData
import com.flynaut.healthtag.util.SavedData.PREF_ID
import com.flynaut.healthtag.util.SavedData.USER_ID
import kotlinx.coroutines.launch

class CardViewModel(private val apiService: ApiService) : ViewModel() {

    private val add_card_api_response: MutableLiveData<AddCardResponse> = MutableLiveData()
    private val create_Customer_Response: MutableLiveData<CreateCustomerResponse> =
        MutableLiveData()
    val addCardApiResponse: LiveData<AddCardResponse> get() = add_card_api_response
    val createCustomerResponse: LiveData<CreateCustomerResponse> get() = create_Customer_Response

    private val _toastMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsg: LiveData<Event<String>> get() = _toastMsg


    private val _toastMsgError: MutableLiveData<Event<String>> = MutableLiveData()
    val toastMsgError: LiveData<Event<String>> get() = _toastMsgError

    private val get_all_card_list: MutableLiveData<List<CardDetails>> = MutableLiveData()
    val getAllCardList: LiveData<List<CardDetails>> get() = get_all_card_list

    private val get_card_api_response: MutableLiveData<CardDetailsResponse> = MutableLiveData()
    val getCardApiResponse: LiveData<CardDetailsResponse> get() = get_card_api_response

    private val _delete_card_api_response: MutableLiveData<DeleteCardResponse> = MutableLiveData()
    val deleteCardApiResponse: LiveData<DeleteCardResponse> get() = _delete_card_api_response

    fun addCard(fields: Map<String, String>) = viewModelScope.launch {
        val response = apiService.addCard(USER_ID, fields)
        if (response.isSuccessful) {
            add_card_api_response.value = response.body()
            Log.d("get", "addCard: $response")
        } else {
            _toastMsg.postValue(
                Event(response.message() ?: R.string.something_went_wrong.toString())
            )
            Log.d("error", "addCard: ${response}")
        }
    }

    fun createCustomer(fields: CreateCustomerRequest) = viewModelScope.launch {
        val response = apiService.createCustomer(USER_ID, fields)
        if (response.isSuccessful) {
            create_Customer_Response.value = response.body()
            Log.d("get", "create customer: $response")
        } else {
            _toastMsg.postValue(
                Event(response.message() ?: R.string.something_went_wrong.toString())
            )
            Log.d("error", "create customer: ${response}")
        }
    }

    //step 2
    fun createCard(fields: CreateCardRequest) = viewModelScope.launch {
        val response = apiService.createCard(PREF_ID, fields)
        if (response.isSuccessful) {
            //   create_Customer_Response.value = response.body()
            Log.d("get", "create customer: $response")
            _toastMsg.postValue(
                Event("Card added successfully")
            )
        } else {
            _toastMsgError.postValue(
                Event(response.message() ?: R.string.something_went_wrong.toString())
            )
            Log.d("error", "create customer: ${response}")
        }
    }


    //For fetching all card
    fun getAllCardDetails(id:String,cardReq: GetCardRequest) = viewModelScope.launch {
        //var id=PrefsManager.get().getString(PrefsManager._ID,"")
        val response = apiService.getCardData( id, cardReq)
        if (response.isSuccessful) {
            get_all_card_list.value = response.body()?.data
            Log.d("get", "getAllCardDetails: $response")
            //_toastMsg.postValue(Event("No card added"))
        } else {
            if (response.code() == 404) {
                _toastMsg.postValue(Event("No card added"))
            } else {
                _toastMsg.postValue(
                    Event(
                        response.message() ?: R.string.something_went_wrong.toString()
                    )
                )
                Log.d("error", "getAllCardDetails: $response")
            }

        }


    }

    //For single card fetching
    fun getCardDetails(cardId: String) = viewModelScope.launch {
        val response = apiService.getCardDetails(USER_ID, cardId)
        if (response.isSuccessful) {
            get_card_api_response.value = response.body()
            Log.d("get", "getCardDetails: $response")
        } else {
            _toastMsg.postValue(
                Event(
                    response.message() ?: R.string.something_went_wrong.toString()
                )
            )
            Log.d("error", "getCardDetails: $response")
        }
    }

    fun deleteCard(cardDelete: DeleteCardRequest) = viewModelScope.launch {
        val response = apiService.deleteCard(PREF_ID, cardDelete)
        if (response.isSuccessful) {
            _delete_card_api_response.value = response.body()
            updateList()
            Log.d("get", "deleteCard: $response")
        } else {
            _toastMsg.postValue(
                Event(
                    response.message() ?: R.string.something_went_wrong.toString()
                )
            )
            Log.d("error", "deleteCard: $response")
        }
    }

    private fun updateList() {
        getAllCardDetails(PREF_ID,GetCardRequest(
            SavedData.PREF_CREATE_CUSTOMER_KEY
        ))
    }

}
