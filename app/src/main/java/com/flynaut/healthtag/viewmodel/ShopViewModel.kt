package com.flynaut.healthtag.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.request.AddToCartRequest
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.model.response.AddCartResponse
import com.flynaut.healthtag.model.response.CategoryResponse
import com.flynaut.healthtag.model.response.ShopListResponse
import com.flynaut.healthtag.util.Event
import com.flynaut.healthtag.util.SavedData.USER_ID
import kotlinx.coroutines.launch

class ShopViewModel(private val apiService: ApiService): ViewModel() {

    private val _apiResponse: MutableLiveData<ShopListResponse> = MutableLiveData()
    private val _cartResponse: MutableLiveData<AddCartResponse> = MutableLiveData()
    private val _categoryResponse: MutableLiveData<ShopListResponse> = MutableLiveData()

    private val _toastMsg: MutableLiveData<Event<Any>> = MutableLiveData()
    val toastMsg: LiveData<Event<Any>>
        get() = _toastMsg

    val apiResponse: LiveData<ShopListResponse>
        get() = _apiResponse

    val cartResponse: LiveData<AddCartResponse>
        get() = _cartResponse

    val catResponse: LiveData<ShopListResponse>
        get() = _categoryResponse

    fun getShopProduct() = viewModelScope.launch {
        val response = apiService.getShopProducts(USER_ID)
        if (response.isSuccessful)
            _apiResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }

    fun addToCart(addToCartRequest: AddToCartRequest) = viewModelScope.launch {
//        try {
        val response = apiService.addToCart(USER_ID, addToCartRequest)
        if (response.isSuccessful) {
            _cartResponse.value = response.body()
            Log.d("cart", "addToCartResponse: $response ")
        } else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }


    fun getProductByCategory(category_id: String) = viewModelScope.launch {
        val response = apiService.getShopByCategory(USER_ID,category_id)
        if (response.isSuccessful){
            _categoryResponse.value = response.body()
        }else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
        }
    }


}