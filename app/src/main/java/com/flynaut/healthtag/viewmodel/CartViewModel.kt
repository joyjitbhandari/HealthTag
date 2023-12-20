package com.flynaut.healthtag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.request.CreateChargeRequest
import com.flynaut.healthtag.model.request.GetCardRequest
import com.flynaut.healthtag.model.request.PlaceOrderRequest
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.model.response.AddCartResponse
import com.flynaut.healthtag.model.response.CardDetails
import com.flynaut.healthtag.model.response.CartItemResponse
import com.flynaut.healthtag.model.response.GetCartResponse
import com.flynaut.healthtag.util.Event
import com.flynaut.healthtag.util.SavedData.PREF_ID
import kotlinx.coroutines.launch

class CartViewModel(private val apiService: ApiService): ViewModel() {


    private val _getCartResponse: MutableLiveData<CartItemResponse> = MutableLiveData()
    private val _cartResponse: MutableLiveData<AddCartResponse> = MutableLiveData()
    private val _updateCartResponse: MutableLiveData<AddCartResponse> = MutableLiveData()
    private val _deleteCartResponse: MutableLiveData<GetCartResponse> = MutableLiveData()
    private val _deleteCartProductResponse: MutableLiveData<AddCartResponse> = MutableLiveData()

    private val get_all_card_list: MutableLiveData<List<CardDetails>> = MutableLiveData()
    val getAllCardList: LiveData<List<CardDetails>> get() = get_all_card_list

    private val _toastMsg : MutableLiveData<Event<Any>> = MutableLiveData()


    private val _toastMsgSuccess : MutableLiveData<Event<Any>> = MutableLiveData()
    private val _placeOrderSuccess : MutableLiveData<Event<Any>> = MutableLiveData()
    val toastMsgSuccess : LiveData<Event<Any>>
        get() = _toastMsgSuccess
    val placeOrderSuccess : LiveData<Event<Any>>
        get() = _placeOrderSuccess

    val toastMsg : LiveData<Event<Any>>
        get() = _toastMsg

    val getCartResponse: LiveData<CartItemResponse>
        get() = _getCartResponse

    val cartResponse: LiveData<AddCartResponse>
        get() = _cartResponse

    val updateCartResponse: LiveData<AddCartResponse>
        get() = _updateCartResponse

    val deleteCartResponse: LiveData<GetCartResponse>
        get() = _deleteCartResponse

    val deleteCartProductResponse: LiveData<AddCartResponse>
        get() = _deleteCartProductResponse

    fun getCart() = viewModelScope.launch {
        Log.d("", "updateCart: $PREF_ID")
        val response = apiService.getCart(PREF_ID)
        if(response.isSuccessful)
            _getCartResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }

    fun clearCart() = viewModelScope.launch {
        val response = apiService.clearCart(PREF_ID)
        if(response.isSuccessful)
            _deleteCartResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }

    fun deleteCartProduct(productId: String) = viewModelScope.launch {
        val response = apiService.deleteCartProduct(PREF_ID,productId)
        if(response.isSuccessful){
            _deleteCartProductResponse.value = response.body()
            updateList()
        }
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }

    fun updateCart( shopId : String, fields : Map<String, String>) = viewModelScope.launch {

        val response = apiService.updateCart(PREF_ID, shopId, fields)
        if(response.isSuccessful)
            _updateCartResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }

//    fun addToCart( productId : String) = viewModelScope.launch {
//        try {
//            val response = apiService.addToCart(USER_ID, productId)
//            if (response.isSuccessful)
//                _cartResponse.value = response.body()
//            else
//                _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
//        }catch (e:Exception){
//            e.printStackTrace()
//        }
//    }
fun createChargeApi(id:String,cardReq: CreateChargeRequest) = viewModelScope.launch {
    //var id=PrefsManager.get().getString(PrefsManager._ID,"")
    val response = apiService.createCharge( id, cardReq)
    if (response.isSuccessful) {
       // get_all_card_list.value = response.body()?.data
        Log.d("get", "getAllCardDetails: $response")
        _toastMsgSuccess.postValue(Event("Payment done"))
    } else {
        if (response.code() == 404) {
            _toastMsg.postValue(Event("Error"))
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


    fun placeOrderApi(cardReq: PlaceOrderRequest) = viewModelScope.launch {
    val response = apiService.placeOrder(PREF_ID, cardReq)
    if (response.isSuccessful) {
       // get_all_card_list.value = response.body()?.data
        Log.d("get", "getAllCardDetails: $response")
        _placeOrderSuccess.postValue(Event("Payment Successfully Done"))
    } else {
        if (response.code() == 404) {
            _toastMsg.postValue(Event("Error"))
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
    private fun updateList() {
        getCart()
    }


}