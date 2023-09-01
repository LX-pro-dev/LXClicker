package com.example.lxclicker.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountViewModel : ViewModel() {

    private val _countState = MutableLiveData(0)
    val countState: LiveData<Int>
        get() = _countState

    private val _mode = MutableLiveData<CountMode>()
    val mode: LiveData<CountMode>
        get() = _mode

    init {
        // Initialize the count state when ViewModel is created
        _countState.value = 0
        _mode.value = CountMode.INCREMENT
    }

    fun incrementCount() {
        val currentCount = _countState.value ?: 0
        _countState.value = currentCount + 1
    }

    fun decrementCount() {
        val currentCount = _countState.value ?: 0
        if (currentCount > 0) {
            _countState.value = currentCount - 1
        }
    }

    fun switchMode() {
        _mode.value = if (_mode.value == CountMode.INCREMENT) CountMode.DECREMENT else CountMode.INCREMENT
    }

    fun resetCount() {
        _countState.value = 0
        _mode.value = CountMode.INCREMENT
    }

    fun setInitialValueAndMode(initialValue: Int, mode: CountMode) {
        _countState.value = initialValue
        _mode.value = mode
    }
}

enum class CountMode {
    INCREMENT,               // Increment mode
    DECREMENT                // Decrement mode
}