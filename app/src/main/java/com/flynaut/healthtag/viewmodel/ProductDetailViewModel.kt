package com.flynaut.healthtag.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.model.request.AddRatingRequest
import com.flynaut.healthtag.model.response.AllRatingResponse
import com.flynaut.healthtag.model.response.BaseResponse
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.model.response.ProductDetailResponse
import com.flynaut.healthtag.model.response.RecommendedProductItems
import com.flynaut.healthtag.util.Event
import com.flynaut.healthtag.util.SavedData.USER_ID
import kotlinx.coroutines.launch

class ProductDetailViewModel(private val apiService: ApiService): ViewModel() {

    private val _apiResponse: MutableLiveData<ProductDetailResponse> = MutableLiveData()
    private val _recommendedProducts: MutableLiveData<List<RecommendedProductItems>> = MutableLiveData()
    private val _toastMsg : MutableLiveData<Event<Any>> = MutableLiveData()
    val toastMsg : LiveData<Event<Any>>
        get() = _toastMsg

    val apiResponse: LiveData<ProductDetailResponse>
        get() = _apiResponse

    val recommendedProducts: LiveData<List<RecommendedProductItems>>
        get() = _recommendedProducts

    private val _addRatingResponse: MutableLiveData<BaseResponse> = MutableLiveData()
    val addRatingResponse: LiveData<BaseResponse>
        get() = _addRatingResponse

    private val _allRatingResponse: MutableLiveData<AllRatingResponse> = MutableLiveData()
    val allRatingResponse: LiveData<AllRatingResponse>
        get() = _allRatingResponse

    fun getProductDetail(productId : String) = viewModelScope.launch {
        val response = apiService.getProductDetail(productId)
        if(response.isSuccessful){
            _apiResponse.value = response.body()

        }
        else{
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
//            Log.d("TAG1", "getProductDetail: ${response.errorBody()}, ${response.code()}, ${response.message()}")
        }
    }

    fun getRecommendedProducts(categoryId: String, productId: String) = viewModelScope.launch {
        try {
            val response = apiService.getRecommendedProducts(USER_ID, categoryId, productId)
            if (response.isSuccessful)
                _recommendedProducts.value =response.body()?.data
             else
                _toastMsg.postValue(Event(response.message() ?: "Something went wrong"))

        } catch (e: Exception) {
            _toastMsg.postValue(Event(R.string.something_went_wrong))
            println("error here"+e.message)
        }
    }

    fun addRating(productId: String, addRatingRequest: AddRatingRequest) = viewModelScope.launch {
        val response = apiService.addRating(USER_ID,productId,addRatingRequest)
        if(response.isSuccessful)
            _addRatingResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }

//    fun getAllRating() = viewModelScope.launch {
//        val response = apiService.getAllRating(USER_ID)
//        if(response.isSuccessful)
//            _allRatingResponse.value = response.body()
//        else
//            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
//    }

}