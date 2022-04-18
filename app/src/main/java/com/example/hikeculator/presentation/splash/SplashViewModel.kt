package com.example.hikeculator.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {

    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean>
    get() = _status

    fun startInit() {
        _status.value = false
        viewModelScope.launch {
            delay(TIME_DELAY)
            _status.value = true
        }
    }

    companion object {
        const val TIME_DELAY = 0L
    }
}