package com.tarren.personalquiznew.data.repo

import com.tarren.personalquiznew.data.model.Quiz
import java.io.InputStream

interface QuizRepo {
    suspend fun createQuiz(quiz: Quiz)
}