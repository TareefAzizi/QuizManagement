package com.tarren.personalquiznew.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.tarren.personalquiznew.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepoImpl(
    firestore: FirebaseFirestore,// Use FirebaseFirestore instance
    private val storageReference: StorageReference

) : UserRepo {

    // Access the specific collections from Firestore
    private val usersCollection = firestore.collection("users")
    override suspend fun addUser(id: String, user: User) {
        try {
            usersCollection.document(id).set(user.toHashMap()).await()
        } catch (e: Exception) {
            // Handle exceptions appropriately
        }
    }

    override suspend fun getUser(id: String): User? {
        return try {
            val snapshot = usersCollection.document(id).get().await()
            snapshot.data?.let { data ->
                data["id"] = snapshot.id
                User.fromHashMap(data)
            }
        } catch (e: Exception) {
            // Handle exceptions appropriately
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
}