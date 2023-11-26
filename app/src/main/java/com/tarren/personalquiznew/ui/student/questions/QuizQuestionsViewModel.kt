package com.tarren.personalquiznew.ui.student.questions

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.tarren.personalquiznew.data.model.QuizAttempt
import com.tarren.personalquiznew.data.model.QuizQuestion
import com.tarren.personalquiznew.data.repo.QuizRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.InputStream
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

@HiltViewModel
class QuizQuestionsViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    private val firestore: FirebaseFirestore,

    ) : ViewModel() {

    private val _quizQuestions = MutableLiveData<List<QuizQuestion>>()
    val quizQuestions: LiveData<List<QuizQuestion>> = _quizQuestions
    private val _timeLimit = MutableLiveData<Int>()
    val timeLimit: LiveData<Int> = _timeLimit

    fun fetchQuizQuestions(quizId: String) {
        viewModelScope.launch {
            try {
                val csvInputStream = quizRepo.fetchCsvFile(quizId)
                Log.d("QuizQuestionsVM", "CSV file fetched for quiz ID: $quizId")
                val questions = parseCsv(csvInputStream)
                Log.d("QuizQuestionsVM", "Parsed questions: $questions")
                _quizQuestions.postValue(questions)
            } catch (e: Exception) {
                Log.e("QuizQuestionsVM", "Error fetching or parsing CSV: ${e.message}", e)
            }
        }
    }


    private fun parseCsv(inputStream: InputStream): List<QuizQuestion> {
        val questions = mutableListOf<QuizQuestion>()
        inputStream.bufferedReader().use { reader ->
            val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())

            for (csvRecord in csvParser) {
                val optionsString = csvRecord.get("options").split(",").map { it.trim().substring(3) } // Remove "A) ", "B) " etc.
                val correctAnswerIndex = when (csvRecord.get("correctAnswer")) {
                    "A" -> 0
                    "B" -> 1
                    "C" -> 2
                    "D" -> 3
                    else -> -1
                }
                val question = QuizQuestion(
                    questionId = csvRecord.get("questionId"),
                    quizTitle = csvRecord.get("quizTitle"),
                    question = csvRecord.get("question"),
                    options = optionsString,
                    correctAnswer = if (correctAnswerIndex != -1) optionsString[correctAnswerIndex] else ""
                )
                questions.add(question)
            }
        }
        return questions
    }

    fun fetchQuizDetails(quizId: String) {
        fetchQuizQuestions(quizId)
        fetchTimeLimit(quizId)
    }

    private fun fetchTimeLimit(quizId: String) {
        viewModelScope.launch {
            try {
                val limit = quizRepo.fetchQuizTimeLimit(quizId)
                _timeLimit.postValue(limit)
            } catch (e: Exception) {
                Log.e("QuizQuestionsVM", "Error fetching time limit: ${e.message}", e)
            }
        }
    }

    fun fetchUserName(userId: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val userDocument = firestore.collection("users").document(userId).get().await()
                val userName = userDocument.getString("name") ?: "Unknown User"
                onResult(userName)
            } catch (e: Exception) {
                Log.e("QuizQuestionsVM", "Error fetching user name: ${e.message}", e)
                onResult("Unknown User")
            }
        }
    }


    fun saveQuizAttempt(quizAttempt: QuizAttempt) {
        viewModelScope.launch {
            try {
                quizRepo.saveQuizAttempt(quizAttempt)
                // Additional logic if needed after saving
            } catch (e: Exception) {
                // Handle the error appropriately
            }
        }

    }


}
