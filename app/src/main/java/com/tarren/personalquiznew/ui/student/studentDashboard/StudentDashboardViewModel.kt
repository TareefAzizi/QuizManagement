package com.tarren.personalquiznew.ui.student.studentDashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarren.personalquiznew.data.model.Quiz
import com.tarren.personalquiznew.data.model.QuizAttempt
import com.tarren.personalquiznew.data.repo.QuizRepo
import com.tarren.personalquiznew.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentDashboardViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val quizRepo: QuizRepo

) : ViewModel() {
    val joinedQuizzes = MutableLiveData<List<Quiz>>()

    fun fetchJoinedQuizzes(userId: String) {
        viewModelScope.launch {
            try {
                val quizzes = userRepo.getQuizzesForUser(userId)
                val attempts = userRepo.getQuizAttemptsForUser(userId)

                quizzes.forEach { quiz ->
                    val attempt = attempts.find { it.quizId == quiz.quizId }
                    if (attempt != null) {
                        quiz.isTaken = true
                        quiz.correctAnswers = attempt.correctAnswers
                        quiz.totalQuestions = attempt.totalQuestions
                    }
                }

                joinedQuizzes.value = quizzes
                Log.d("StudentDashboardVM", "Fetched quizzes: ${quizzes.size}")
            } catch (e: Exception) {
                Log.e("StudentDashboardVM", "Error fetching quizzes: ${e.message}")
            }
        }
    }





    fun joinQuiz(userId: String, quizId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                userRepo.addQuizToUser(userId, quizId)
                onSuccess()
                Log.d("StudentDashboardVM", "Joined quiz successfully")
            } catch (e: Exception) {
                Log.e("StudentDashboardVM", "Error joining quiz: ${e.message}")
                onError(e.message ?: "Error joining quiz")
            }
        }
    }



    }

