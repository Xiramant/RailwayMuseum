package com.omsk.railwaymuseum.viewmodels

import androidx.lifecycle.*
import com.omsk.railwaymuseum.data.GameType
import com.omsk.railwaymuseum.net.game.GameApi
import com.omsk.railwaymuseum.net.game.GameListModel
import com.omsk.railwaymuseum.net.game.asGameListModel
import kotlinx.coroutines.launch

class GameListViewModel: ViewModel() {

    private val _response = MutableLiveData<List<GameListModel>>()

    val response: LiveData<List<GameListModel>>
        get() = _response

    init {
        getGameList()
    }

    private fun getGameList() {
        viewModelScope.launch {
            try {
                _response.value = GameApi.retrofitListService.getGameListApi()
                    .asGameListModel()
                    .filter {it.type != GameType.UNDEFINED }
            } catch (e: Exception) {
                _response.value = ArrayList()
            }
        }
    }



    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameListViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct GameListViewModel")
        }
    }
}

