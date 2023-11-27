package com.tarren.personalquiznew.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _quizUpdated = MutableLiveData<Boolean>()
    val quizUpdated: LiveData<Boolean> = _quizUpdated

    fun notifyQuizUpdated(updated: Boolean = true) {
        _quizUpdated.value = updated
    }
}

