package com.omsk.railwaymuseum.viewmodels

import androidx.lifecycle.*
import com.omsk.railwaymuseum.net.review.ReviewApi
import com.omsk.railwaymuseum.net.review.ReviewInsertApi
import com.omsk.railwaymuseum.net.review.ReviewModel
import kotlinx.coroutines.launch

const val REVIEW_INSERT_GOAL = "review_insert"
const val SUCCESSFUL_RESPONSE = "Отзыв успешно добавлен в систему."
const val UNSUCCESSFUL_RESULT = "Ошибка передачи данных."

class ReviewViewModel: ViewModel() {

    private val _response = MutableLiveData<List<ReviewModel>>()
    val response: LiveData<List<ReviewModel>>
        get() = _response

    private val _errorDescription = MutableLiveData<String>()
    val errorDescription: LiveData<String>
        get() = _errorDescription

    private val _reviewInsert = MutableLiveData(false)
    val reviewInsert: LiveData<Boolean>
        get() = _reviewInsert

    init {
        getReview()
    }

    fun getReview() {
        viewModelScope.launch {
            try {
                _response.value = ReviewApi.retrofitService.getReviewApi()
                _reviewInsert.value = false
            } catch (e: Exception) {
                _response.value = ArrayList()
            }
        }
    }

    fun insertReview(name: String, review: String) {
        viewModelScope.launch {
            try {
                val response = ReviewInsertApi.retrofitInsertService.insertReviewApi(REVIEW_INSERT_GOAL, name, review)
                if (response == SUCCESSFUL_RESPONSE) {
                    _reviewInsert.value = true
                } else {
                    _errorDescription.value = response
                }
            } catch (e: Exception) {
                _errorDescription.value = UNSUCCESSFUL_RESULT
            }
        }
    }



    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReviewViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct ReviewViewModel")
        }
    }
}

