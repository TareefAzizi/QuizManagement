package com.tarren.personalquiznew.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.tarren.personalquiznew.data.model.Quiz
import kotlinx.coroutines.tasks.await
import java.util.UUID

class QuizRepoImpl(
    private val firestore: FirebaseFirestore,
    private val storageReference: StorageReference

) : QuizRepo {

    private val quizzesCollection = firestore.collection("quiz")

    override suspend fun createQuiz(quiz: Quiz) {
        try {
            // Generate a unique ID for the quiz
            val quizId = UUID.randomUUID().toString()
            val quizWithCustomId = quiz.copy(quizId = quizId)

            // Set the document with the generated ID
            quizzesCollection.document(quizId).set(quizWithCustomId.toHashMap()).await()

            Log.d("QuizRepo", "Quiz created with ID: $quizId")
        } catch (e: Exception) {
            Log.e("QuizRepo", "Error creating quiz: ${e.message}", e)
            throw e
        }
    }
}
