package com.omsk.railwaymuseum.viewmodels

import androidx.lifecycle.*
import com.omsk.railwaymuseum.net.game.GameApi
import com.omsk.railwaymuseum.net.game.GameRulesModel
import com.omsk.railwaymuseum.net.game.asGameRulesModel
import com.omsk.railwaymuseum.net.game.getEmptyGameRulesModel
import kotlinx.coroutines.launch

class GameRulesViewModel(private val gameNameId: Int): ViewModel() {

    private val _response = MutableLiveData<GameRulesModel>()

    val response: LiveData<GameRulesModel>
        get() = _response

    init {
        getGameRules()
    }

    private fun getGameRules() {
        viewModelScope.launch {
            try {
                _response.value = GameApi.retrofitRulesService.getGameRulesApi(gameNameId)
                    .asGameRulesModel()
            } catch (e: Exception) {
                _response.value = getEmptyGameRulesModel()
            }
        }
    }

    class Factory(private val gameNameId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameRulesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameRulesViewModel(gameNameId) as T
            }
            throw IllegalArgumentException("Unable to construct GameRulesViewModel")
        }
    }
}