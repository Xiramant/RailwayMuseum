package com.omsk.railwaymuseum.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omsk.railwaymuseum.net.slider.HomeSliderImageApi
import com.omsk.railwaymuseum.net.slider.HomeSliderImageModel
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _response = MutableLiveData<List<HomeSliderImageModel>>()

    val response: LiveData<List<HomeSliderImageModel>>
        get() = _response

    init {
        getHomeSliderImageList()
    }

    private fun getHomeSliderImageList() {
        viewModelScope.launch {
            try {
                _response.value = HomeSliderImageApi.retrofitService.getEventApi()
            } catch (e: Exception) {
                _response.value = ArrayList()
            }
        }
    }
}