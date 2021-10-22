package com.omsk.railwaymuseum.viewmodels

import androidx.lifecycle.*
import com.omsk.railwaymuseum.net.game.GameQuestionsApi
import com.omsk.railwaymuseum.net.game.GameQuestionsModel
import com.omsk.railwaymuseum.net.game.GameRulesModel
import kotlinx.coroutines.launch

class GameQuestionsViewModel(private val game: GameRulesModel): ViewModel() {

    private val splitTag = "\r\n"
    val questionsNumber = game.questionsNumber
    private lateinit var fullQuestionList: List<GameQuestionsModel>
    private lateinit var gameQuestionList: List<GameQuestionsModel>

    private val _questionIndex = MutableLiveData(0)
    val questionIndex: LiveData<Int>
        get() = _questionIndex

    private val _rightAnswerNumber = MutableLiveData(0)
    val rightAnswerNumber: LiveData<Int>
        get() = _rightAnswerNumber

    private val _currentGameQuestion = MutableLiveData<GameQuestionsModel>()
    val currentGameQuestion: LiveData<GameQuestionsModel>
        get() = _currentGameQuestion

    private val _moveToResult = MutableLiveData(false)
    val moveToResult: LiveData<Boolean>
        get() = _moveToResult

    init {
        getGameQuestions()
    }

    private fun getGameQuestions() {
        viewModelScope.launch {
            try {
                fullQuestionList = GameQuestionsApi.retrofitService.getGameQuestionsApi(game.id)
                shuffledAndSetQuestList()
            } catch (e: Exception) {
                gameQuestionList = ArrayList()
            }
        }
    }

    private fun shuffledAndSetQuestList() {
        gameQuestionList = fullQuestionList.shuffled()
        setQuestion()
    }

    fun testRightAnswer(selectAnswerId: Int) {
        if(currentGameQuestion.value!!.quizAnswer[selectAnswerId] == currentGameQuestion.value!!.answer) {
            _rightAnswerNumber.value = _rightAnswerNumber.value!!.inc()
        }
    }

    fun setQuestion() {
        _questionIndex.value?.let {
            if (it < game.questionsNumber) {
                _currentGameQuestion.value = GameQuestionsModel(
                        gameQuestionList[it].id,
                        gameQuestionList[it].question,
                        gameQuestionList[it].image,
                        gameQuestionList[it].answer.split(splitTag)[0],
                        gameQuestionList[it].answer.split(splitTag).shuffled()
                )
                _questionIndex.value = it.inc()
            } else {
                _moveToResult.value = true
            }
        }
    }

    fun reset() {
        _questionIndex.value = 0
        _rightAnswerNumber.value = 0
        _moveToResult.value = false
        shuffledAndSetQuestList()
    }


    class Factory(private val game: GameRulesModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameQuestionsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameQuestionsViewModel(game) as T
            }
            throw IllegalArgumentException("Unable to construct GameQuestionsViewModel")
        }
    }
}