package com.tarren.personalquiznew.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.tarren.personalquiznew.data.model.Quiz
import com.tarren.personalquiznew.data.model.QuizAttempt
import com.tarren.personalquiznew.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepoImpl(
    private val firestore: FirebaseFirestore,
    private val storageReference: StorageReference

) : UserRepo {

    private val usersCollection = firestore.collection("users")
    override suspend fun addUser(id: String, user: User) {
        try {
            usersCollection.document(id).set(user.toHashMap()).await()
        } catch (e: Exception) {
            // Handle exceptions appropriately
        }
    }

    override suspend fun getUser(UID: String): User? {
        return try {
            val snapshot = usersCollection.document(UID).get().await()
            if (snapshot.exists()) {
                Log.d("UserRepo", "User data found for UID: $UID")
                val user = snapshot.toObject(User::class.java)
                Log.d("UserRepo", "Fetched user: ${user.toString()} with role: ${user?.role}")
                user
            } else {
                Log.d("UserRepo", "No user found with UID: $UID")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepo", "Error fetching user with UID $UID: ${e.message}", e)
            null
        }
    }





    override suspend fun updateUser(id: String, user: User) {
        try {
            usersCollection.document(id).set(user.toHashMap()).await()
        } catch (e: Exception) {
            // Handle exceptions appropriately
        }
    }

    override suspend fun addQuizToUser(userId: String, quizId: String) {
        try {
            val userDocRef = usersCollection.document(userId)
            val userSnapshot = userDocRef.get().await()
            if (userSnapshot.exists()) {
                val joinedQuizzes = (userSnapshot["joinedQuizzes"] as? List<String>)?.toMutableList() ?: mutableListOf()
                if (quizId !in joinedQuizzes) {
                    joinedQuizzes.add(quizId)
                    userDocRef.update("joinedQuizzes", joinedQuizzes).await()
                }
            } else {
                // Handle the case where the user does not exist
            }
        } catch (e: Exception) {
            Log.e("UserRepo", "Error in addQuizToUser: ${e.message}", e)
            // Handle exceptions (e.g., log the error)
        }
    }


    override suspend fun getQuizzesForUser(userId: String): List<Quiz> {
        return try {
            // Fetch the user to get the list of joined quizzes
            val userSnapshot = usersCollection.document(userId).get().await()
            val joinedQuizIds = userSnapshot.data?.get("joinedQuizzes") as? List<String> ?: emptyList()

            // Fetch each quiz from the "quiz" collection based on the IDs
            joinedQuizIds.mapNotNull { quizId ->
                firestore.collection("quiz").document(quizId).get().await().toObject(Quiz::class.java)
            }
        } catch (e: Exception) {
            Log.e("UserRepo", "Error in getQuizzesForUser: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getQuizAttemptsForUser(userId: String): List<QuizAttempt> {
        val attemptsCollection = firestore.collection("quizAttempts")
        val attemptsSnapshot = attemptsCollection.whereEqualTo("userId", userId).get().await()
        val attempts = attemptsSnapshot.toObjects(QuizAttempt::class.java)
        Log.d("UserRepoImpl", "Fetched ${attempts.size} quiz attempts for user $userId")
        return attempts
    }


    override suspend fun addOrUpdateUser(user: User) {
        try {
            // Assuming user.id is the UID from Firebase Authentication
            val userDocRef = usersCollection.document(user.id!!)
            userDocRef.set(user.toHashMap()).await()
            Log.d("UserRepoImpl", "User with UID ${user.id} added/updated successfully")
        } catch (e: Exception) {
            Log.e("UserRepoImpl", "Error adding/updating user with UID ${user.id}: ${e.message}", e)
        }
    }

    override suspend fun getUserRole(id: String): String? {
        Log.d("UserRepoImpl", "Fetching role for ID: $id")
        return try {
            val querySnapshot = usersCollection.whereEqualTo("id", id).get().await()
            if (!querySnapshot.isEmpty) {
                val user = querySnapshot.documents.first().toObject(User::class.java)
                val role = user?.role
                Log.d("UserRepoImpl", "Role for ID $id found: $role")
                role
            } else {
                Log.d("UserRepoImpl", "No user found with ID: $id")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepoImpl", "Error fetching user role with ID $id: ${e.message}", e)
            null
        }


    }

    override suspend fun getUserRoleByEmail(email: String): String? {
        Log.d("UserRepoImpl", "Fetching role for email: $email")
        return try {
            val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()
            querySnapshot.documents.forEach { document ->
                Log.d("UserRepoImpl", "Fetched document: ${document.data}")
                val user = document.toObject(User::class.java)
                if (!user?.role.isNullOrEmpty()) {
                    Log.d("UserRepoImpl", "Role for email $email found: ${user?.role}")
                    return user?.role
                }
            }
            Log.d("UserRepoImpl", "No user with a non-empty role found for email: $email")
            null
        } catch (e: Exception) {
            Log.e("UserRepoImpl", "Error fetching user role with email $email: ${e.message}", e)
            null
        }
    }
}