package com.tarren.personalquiznew.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.tarren.personalquiznew.data.model.Quiz
import kotlinx.coroutines.tasks.await
import java.io.InputStream

class QuizRepoImpl(
    firestore: FirebaseFirestore,// Use FirebaseFirestore instance
    private val storageReference: StorageReference
) : QuizRepo {

    private val quizzesCollection = firestore.collection("quiz")

    override suspend fun createQuiz(quiz: Quiz) {
        try {
            // Omit the 'id' parameter and let Firebase Firestore generate the ID
            val quizWithAutoId = quiz.copy(quizId = "") // Ensure 'quizId' is an empty string
            val docRef = quizzesCollection.add(quizWithAutoId.toHashMap()).await()
            val quizId = docRef.id
            Log.d("QuizRepo", "Quiz created with ID: $quizId")
        } catch (e: Exception) {
            Log.e("QuizRepo", "Error creating quiz: ${e.message}", e)
            throw e
        }
    }


}
