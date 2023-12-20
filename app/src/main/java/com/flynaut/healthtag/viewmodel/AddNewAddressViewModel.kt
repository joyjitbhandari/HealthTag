package com.flynaut.healthtag.viewmodel

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.request.AddressRequest
import com.flynaut.healthtag.model.request.GetCardRequest
import com.flynaut.healthtag.model.response.AddAddressResponse
import com.flynaut.healthtag.model.response.AddressResponse
import com.flynaut.healthtag.model.response.CardDetails
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.util.Event
import com.flynaut.healthtag.util.SavedData.USER_ID
import kotlinx.coroutines.launch

class AddNewAddressViewModel(private val apiService: ApiService): ViewModel() {

    private val _apiResponse: MutableLiveData<AddAddressResponse> = MutableLiveData()
    private val _getAddressResponse: MutableLiveData<AddressResponse> = MutableLiveData()

    private val _toastMsg : MutableLiveData<Event<Any>> = MutableLiveData()
    private val _toastMsgCard : MutableLiveData<Event<Any>> = MutableLiveData()
    val toastMsg : LiveData<Event<Any>>
        get() = _toastMsg
    val toastMsgCard : LiveData<Event<Any>>
        get() = _toastMsgCard

    val apiResponse: LiveData<AddAddressResponse>
        get() = _apiResponse

    val getAddressResponse: LiveData<AddressResponse>
        get() = _getAddressResponse

    private val get_all_card_list: MutableLiveData<List<CardDetails>> = MutableLiveData()
    val getAllCardList: LiveData<List<CardDetails>> get() = get_all_card_list
    fun addAddress(addressRequest: AddressRequest) = viewModelScope.launch {
        val response = apiService.addAddress(USER_ID, addressRequest)
        if(response.isSuccessful)
            _apiResponse.value = response.body()
        else
            _toastMsgCard.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }
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

    fun getAddress() = viewModelScope.launch {
        val response = apiService.getAddress(USER_ID)
        if(response.isSuccessful)
            _getAddressResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }

    fun getDefaultAddress() = viewModelScope.launch {
        val response = apiService.defaultAddress(USER_ID)
        if(response.isSuccessful)
            _apiResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }

}