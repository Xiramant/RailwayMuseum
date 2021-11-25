package com.omsk.railwaymuseum.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val PRIMARY_MESSAGE = "Загрузка..."

class ProgressBarViewModel: ViewModel() {
    private val _message = MutableLiveData(PRIMARY_MESSAGE)
    val message: LiveData<String>
        get() = _message

    fun setMessage(messageArg: String) {
        _message.value = messageArg
    }
}