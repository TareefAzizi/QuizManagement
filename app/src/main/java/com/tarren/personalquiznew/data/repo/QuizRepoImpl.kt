package com.tarren.personalquiznew.data.repo

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tarren.personalquiznew.data.model.Quiz
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import java.util.UUID

class QuizRepoImpl(
    private val firestore: FirebaseFirestore,
    private val storageReference: StorageReference

) : QuizRepo {

    private val quizzesCollection = firestore.collection("quiz")

    override suspend fun createQuiz(quiz: Quiz, csvFileUri: Uri) {
        try {
            // Generate a unique ID for the quiz
            val quizId = UUID.randomUUID().toString()

            // Upload CSV file to Firebase Storage
            val csvFileUrl = uploadCsvFile(csvFileUri, quizId)

            // Create a new Quiz object with the CSV file URL and the custom ID
            val quizWithCustomIdAndCsv = quiz.copy(quizId = quizId, csvFileUrl = csvFileUrl)

            // Set the document with the generated ID
            quizzesCollection.document(quizId).set(quizWithCustomIdAndCsv.toHashMap()).await()

            // Set the document with the generated ID
            quizzesCollection.document(quizId).set(quizWithCustomIdAndCsv.toHashMap()).await()

            Log.d("QuizRepo", "Quiz created with ID: $quizId")
        } catch (e: Exception) {
            Log.e("QuizRepo", "Error creating quiz: ${e.message}", e)
            throw e
        }
    }
    private suspend fun uploadCsvFile(fileUri: Uri, quizId: String): String {
        val fileRef = storageReference.child("quizzes/$quizId.csv")
        fileRef.putFile(fileUri).await()
        return fileRef.downloadUrl.await().toString()
    }

    override suspend fun getAllQuizzes(): List<Quiz> {
        return try {
            quizzesCollection
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    document.toObject(Quiz::class.java)?.apply { quizId = document.id }
                }
        } catch (e: Exception) {
            Log.e("QuizRepo", "Error fetching quizzes: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun updateQuizTime(quiz: Quiz) {
        try {
            quizzesCollection.document(quiz.quizId).set(quiz.toHashMap()).await()
        } catch (e: Exception) {
            Log.e("QuizRepo", "Error updating quiz: ${e.message}", e)
            // Handle exceptions appropriately
        }
    }

    override suspend fun fetchCsvFile(quizId: String): InputStream {
        val quizDocumentSnapshot = firestore.collection("quiz").document(quizId).get().await()
        val csvFileUrl = quizDocumentSnapshot.getString("csvFileUrl")
            ?: throw IllegalStateException("CSV file URL not found")

        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(csvFileUrl)
        val streamTask = storageRef.stream.await()
        return streamTask.stream
    }




}
