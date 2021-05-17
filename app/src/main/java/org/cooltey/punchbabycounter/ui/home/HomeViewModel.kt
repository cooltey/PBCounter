package org.cooltey.punchbabycounter.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _leftCounter = MutableLiveData<Int>().apply {
        value = 0
    }
    val leftCounter: LiveData<Int> = _leftCounter


    private val _rightCounter = MutableLiveData<Int>().apply {
        value = 0
    }
    val rightCounter: LiveData<Int> = _rightCounter
}