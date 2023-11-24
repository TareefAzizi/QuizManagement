package com.tarren.personalquiznew.ui.student.studentDashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarren.personalquiznew.data.model.Quiz
import com.tarren.personalquiznew.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentDashboardViewModel @Inject constructor(
    private val userRepo: UserRepo
) : ViewModel() {
    fun joinQuiz(userId: String, quizId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {

        viewModelScope.launch {
            try {
                userRepo.addQuizToUser(userId, quizId)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error joining quiz")
            }
        }
    }

    val joinedQuizzes = MutableLiveData<List<Quiz>>()

    fun fetchJoinedQuizzes(userId: String) {
        viewModelScope.launch {
            try {
                val quizzes = userRepo.getQuizzesForUser(userId)
                joinedQuizzes.value = quizzes
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
