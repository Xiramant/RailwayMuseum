package com.omsk.railwaymuseum.viewmodels

import androidx.lifecycle.*
import com.omsk.railwaymuseum.net.game.GameApi
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

    private val _wrongAnswerToast = MutableLiveData(false)
    val wrongAnswerToast: LiveData<Boolean>
        get() = _wrongAnswerToast

    init {
        getGameQuestions()
    }

    private fun getGameQuestions() {
        viewModelScope.launch {
            try {
                fullQuestionList = GameApi.retrofitQuestionService.getGameQuestionsApi(game.id)
                shuffledAndSetQuestList()
            } catch (e: Exception) {
                gameQuestionList = ArrayList()
            }
        }
    }

    private fun shuffledAndSetQuestList() {
        gameQuestionList = if(game.isShuffle == 1) fullQuestionList.shuffled() else fullQuestionList
        setQuestion()
    }

    fun quizTestRightAnswer(selectAnswerId: Int) {
        if(currentGameQuestion.value!!.quizAnswer[selectAnswerId] == currentGameQuestion.value!!.answer) {
            _rightAnswerNumber.value = _rightAnswerNumber.value!!.inc()
        }
    }

    fun questTestRightAnswer(scanAnswer: String) {
        if(scanAnswer == currentGameQuestion.value!!.answer) {
            _rightAnswerNumber.value = _rightAnswerNumber.value!!.inc()
            setQuestion()
        } else {
            _wrongAnswerToast.value = true
        }
    }

    fun setQuestion() {
        _wrongAnswerToast.value = false

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