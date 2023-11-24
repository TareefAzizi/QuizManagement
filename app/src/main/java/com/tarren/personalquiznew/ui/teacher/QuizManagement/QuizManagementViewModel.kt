package com.tarren.personalquiznew.ui.teacher.QuizManagement

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

    fun createQuiz(quiz: Quiz) {
        viewModelScope.launch {
            try {
                quizRepo.createQuiz(quiz)
                // Handle success, e.g., by updating UI or showing a toast
            } catch (e: Exception) {
                // Handle failure, e.g., by logging or showing an error message
            }
        }
    }
}
