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

    suspend fun createQuiz(quiz: Quiz, uri: Uri?): Boolean {
        return if (uri != null) {
            try {
                quizRepo.createQuiz(quiz, uri)
                true
            } catch (e: Exception) {
                false // Return false if an exception occurs
            }
        } else {
            false // Return false if no file was selected
        }
    }
}
