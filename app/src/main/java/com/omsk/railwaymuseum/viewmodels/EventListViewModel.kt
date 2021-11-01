package com.omsk.railwaymuseum.viewmodels

import androidx.lifecycle.*
import com.omsk.railwaymuseum.net.event.EventListApi
import com.omsk.railwaymuseum.net.event.EventListModel
import kotlinx.coroutines.launch

class EventListViewModel(private val netGoalType: String): ViewModel() {

    private val _response = MutableLiveData<List<EventListModel>>()

    val response: LiveData<List<EventListModel>>
        get() = _response

    init {
        getEventList()
    }

    private fun getEventList() {
        viewModelScope.launch {
            try {
                _response.value = EventListApi.retrofitService.getEventListApi(netGoalType)
            } catch (e: Exception) {
                _response.value = ArrayList()
            }
        }
    }



    class Factory(private val netGoalType: String): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EventListViewModel(netGoalType) as T
            }
            throw IllegalArgumentException("Unable to construct EventListViewModel")
        }
    }
}

