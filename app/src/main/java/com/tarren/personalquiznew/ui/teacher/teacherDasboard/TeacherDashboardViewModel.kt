package com.tarren.personalquiznew.ui.teacher.teacherDasboard
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarren.personalquiznew.data.model.Quiz
import com.tarren.personalquiznew.data.repo.QuizRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TeacherDashboardViewModel @Inject constructor(
    private val quizRepo: QuizRepo // Inject your QuizRepo
) : ViewModel() {

    private val _quizzes = MutableLiveData<List<Quiz>>()
    val quizzes: LiveData<List<Quiz>> = _quizzes

    init {
        fetchQuizzes()
    }

    private fun fetchQuizzes() {
        viewModelScope.launch {
            _quizzes.value = quizRepo.getAllQuizzes()
        }
    }

    fun updateQuizTime(quiz: Quiz) {
        viewModelScope.launch {
            try {
                quizRepo.updateQuizTime(quiz)
                fetchQuizzes()  // Refresh the list of quizzes
            } catch (e: Exception) {
                // Handle any exceptions
                Log.e("TeacherDashboardVM", "Error updating quiz: ${e.message}")
            }
        }
    }

    fun deleteQuiz(quizId: String) {
        viewModelScope.launch {
            try {
                quizRepo.deleteQuiz(quizId)
                fetchQuizzes() // Refresh the list after deletion
            } catch (e: Exception) {
                Log.e("TeacherDashboardVM", "Error deleting quiz: ${e.message}")
            }
        }
    }
}
