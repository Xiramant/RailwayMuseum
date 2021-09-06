package com.omsk.railwaymuseum.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.omsk.railwaymuseum.net.event.EventListApi
import com.omsk.railwaymuseum.net.event.EventListModel
import com.omsk.railwaymuseum.util.TAG
import kotlinx.coroutines.launch
import java.io.IOException

class EventListViewModel: ViewModel() {

    private val _response = MutableLiveData<List<EventListModel>>()

    val response: LiveData<List<EventListModel>>
        get() = _response

    init {
        getEventList()
    }

    private fun getEventList() {
        viewModelScope.launch {
            try {
                _response.value = EventListApi.retrofitService.getEventListApi()
            } catch (e: Exception) {
                _response.value = ArrayList()
            }
        }
    }



    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EventListViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct EventListViewModel")
        }
    }
}

