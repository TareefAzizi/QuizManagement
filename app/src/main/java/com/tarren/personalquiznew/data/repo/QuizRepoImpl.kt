package com.tarren.personalquiznew.data.repo

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tarren.personalquiznew.data.model.Quiz
import com.tarren.personalquiznew.data.model.QuizAttempt
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
            Log.d("QuizRepo", "Error creating quiz: ${e.message}", e)
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
            Log.d("QuizRepo", "Error fetching quizzes: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun updateQuizTime(quiz: Quiz) {
        try {
            quizzesCollection.document(quiz.quizId).set(quiz.toHashMap()).await()
        } catch (e: Exception) {
            Log.d("QuizRepo", "Error updating quiz: ${e.message}", e)
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


    override suspend fun fetchQuizTimeLimit(quizId: String): Int {
        try {
            val quizDocumentSnapshot = firestore.collection("quiz").document(quizId).get().await()
            val timeLimit = quizDocumentSnapshot.getLong("timeLimit")?.toInt()
            return timeLimit ?: throw IllegalStateException("Time limit not found for quiz ID: $quizId")
        } catch (e: Exception) {
            Log.d("QuizRepo", "Error fetching quiz time limit: ${e.message}", e)
            throw e
        }
    }


    override suspend fun saveQuizAttempt(quizAttempt: QuizAttempt) {
        try {
            val attemptsCollection = firestore.collection("quizAttempts")
            attemptsCollection.add(quizAttempt).await()
            Log.d("QuizRepoImpl", "Quiz attempt saved")
        } catch (e: Exception) {
            Log.d("QuizRepoImpl", "Error saving quiz attempt: ${e.message}", e)
            throw e
        }
    }

    override suspend fun deleteQuiz(quizId: String) {
        try {
            // Delete the quiz document from the 'quiz' collection
            firestore.collection("quiz").document(quizId).delete().await()
            Log.d("QuizRepoImpl", "Quiz deleted successfully: $quizId")
        } catch (e: Exception) {
            Log.d("QuizRepoImpl", "Error deleting quiz: ${e.message}", e)
            throw e
        }
    }

    override suspend fun fetchQuizAttempts(quizId: String): List<QuizAttempt> {
        return try {
            firestore.collection("quizAttempts")
                .whereEqualTo("quizId", quizId)
                .get()
                .await()
                .toObjects(QuizAttempt::class.java)
        } catch (e: Exception) {
            Log.d("QuizRepoImpl", "Error fetching quiz attempts: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun fetchLeaderboard(quizId: String): List<Pair<String, Int>> {
        Log.d("QuizRepoImpl", "fetchLeaderboard called with quizId: $quizId")
        return try {
            firestore.collection("quizAttempts")
                .whereEqualTo("quizId", quizId)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    val name = document.getString("studentName") ?: return@mapNotNull null
                    val score = document.getLong("score")?.toInt() ?: return@mapNotNull null
                    Pair(name, score)
                }
                .sortedByDescending { it.second } // Sort by score in descending order
        } catch (e: Exception) {

            Log.d("QuizRepoImpl", "Error fetching leaderboard data: ${e.message}", e)
            emptyList()
        }
    }
    override suspend fun fetchAllQuizLeaderboards(): Map<String, List<Pair<String, Int>>> {
        Log.d("QuizRepoImpl", "fetchAllQuizLeaderboards called")

        val leaderboards = mutableMapOf<String, List<Pair<String, Int>>>()
        val quizzes = getAllQuizzes() // Fetch all quizzes

        quizzes.forEach { quiz ->
            Log.d("QuizRepoImpl", "Fetching leaderboard for quiz: ${quiz.name}")
            val leaderboard = fetchQuizLeaderboard(quiz.quizId)
            leaderboards[quiz.name] = leaderboard // Associate the leaderboard with the quiz name
        }

        return leaderboards
    }

    private suspend fun fetchQuizLeaderboard(quizId: String): List<Pair<String, Int>> {
        Log.d("QuizRepoImpl", "fetchQuizLeaderboard called for quizId: $quizId")
        return try {
            firestore.collection("quizAttempts")
                .whereEqualTo("quizId", quizId)
                .orderBy("correctAnswers", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    val userId = document.getString("userId") // User ID from quiz attempt
                    val correctAnswers = document.getLong("correctAnswers")?.toInt() // Score

                    if (userId != null && correctAnswers != null) {
                        val userName = fetchUserName(userId) // Fetch user name using User ID
                        if (userName.isNotEmpty()) {
                            Pair(userName, correctAnswers)
                        } else {
                            null
                        }
                    } else {
                        null
                    }
                }
        } catch (e: Exception) {
            Log.e("QuizRepoImpl", "Error fetching leaderboard for quizId $quizId: ${e.message}", e)
            emptyList()
        }
    }

    private suspend fun fetchUserName(userId: String): String {
        return try {
            firestore.collection("users")
                .document(userId)
                .get()
                .await()
                .getString("name") ?: ""
        } catch (e: Exception) {
            Log.e("QuizRepoImpl", "Error fetching user name for userId $userId: ${e.message}", e)
            ""
        }
    }



}
