package com.tarren.personalquiznew.data.repo

import android.net.Uri
import com.tarren.personalquiznew.data.model.Quiz
import com.tarren.personalquiznew.data.model.QuizAttempt
import java.io.InputStream

interface QuizRepo {
    suspend fun createQuiz(quiz: Quiz, csvFileUri: Uri)
    suspend fun deleteQuiz(quizId: String)

    suspend fun getAllQuizzes(): List<Quiz>
    suspend fun updateQuizTime(quiz: Quiz)
    suspend fun fetchCsvFile(quizId: String): InputStream
     suspend fun fetchQuizTimeLimit(quizId: String) : Int
    suspend fun saveQuizAttempt(quizAttempt: QuizAttempt)
     suspend fun fetchQuizAttempts(quizId: String): List<QuizAttempt>
    suspend fun fetchLeaderboard(quizId: String): List<Pair<String, Int>>
     suspend fun fetchAllQuizLeaderboards(): Map<String, List<Pair<String, Int>>>




}