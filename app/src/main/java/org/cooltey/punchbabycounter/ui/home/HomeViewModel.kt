package org.cooltey.punchbabycounter.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val maxCount = 50

    private val _leftCounter = MutableLiveData<Int>().apply {
        value = 0
    }
    val leftCounter: LiveData<Int> = _leftCounter
    fun leftCounterIncrement() {
        _leftCounter.value?.let {
            if (it < maxCount) {
                _leftCounter.value = it + 1
            }
        }
    }

    private val _rightCounter = MutableLiveData<Int>().apply {
        value = 0
    }
    val rightCounter: LiveData<Int> = _rightCounter
    fun rightCounterIncrement() {
        _rightCounter.value?.let {
            if (it < maxCount) {
                _rightCounter.value = it + 1
            }
        }
    }
}