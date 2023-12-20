package com.flynaut.healthtag.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flynaut.healthtag.R
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.model.response.BlogResponse
import com.flynaut.healthtag.util.Event
import kotlinx.coroutines.launch

class BlogsViewModel(private val apiService: ApiService): ViewModel() {

    private val _blogsApiResponse: MutableLiveData<BlogResponse> = MutableLiveData()

    private val _toastMsg: MutableLiveData<Event<Any>> = MutableLiveData()
    val toastMsg: LiveData<Event<Any>>
        get() = _toastMsg

    val blogsApiResponse: LiveData<BlogResponse>
        get() = _blogsApiResponse

    fun getAllBlogs() = viewModelScope.launch {
        val response = apiService.getBlogs()
        if (response.isSuccessful)
            _blogsApiResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }

    fun getRelatedBlogs(blog_id: String) = viewModelScope.launch {
        val response = apiService.getRelated(blog_id)
        if (response.isSuccessful)
            _blogsApiResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }

    fun getByCategory(blog_name: String) = viewModelScope.launch {
        val response = apiService.getBlogsByCategory(blog_name)
        if (response.isSuccessful)
            _blogsApiResponse.value = response.body()
        else
            _toastMsg.postValue(Event(response.message() ?: R.string.something_went_wrong))
    }
}