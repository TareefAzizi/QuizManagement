package com.tarren.personalquiznew.ui.teacher.QuizManagement

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarren.personalquiznew.data.model.Quiz
import com.tarren.personalquiznew.data.repo.QuizRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizManagementViewModel @Inject constructor(
    private val quizRepo: QuizRepo
) : ViewModel() {

    fun createQuiz(quiz: Quiz, csvUri: Uri?) {
        viewModelScope.launch {
            if (csvUri != null) {
                quizRepo.createQuiz(quiz, csvUri)
                // Handle success
            } else {
                // Handle case where no file was selected
            }
        }
    }
}
