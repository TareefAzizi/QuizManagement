package com.tarren.personalquiznew.data.model

import com.google.firebase.firestore.FirebaseFirestore

// dat class
data class User(
    val id: String? = null,
    val name: String = "",
    val email: String = "",
    var role: String = "",
    val joinedQuizzes: List<String> = listOf()

) {
    // Function to convert User object to a HashMap
    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "role" to role,
            "joinedQuizzes" to joinedQuizzes
        )
    }



    companion object {
        // Function to create a User object from a HashMap
        fun fromHashMap(hash: Map<String, Any>): User {
            return User(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                email = hash["email"].toString(),
                role = hash["role"].toString(),
                joinedQuizzes = hash["joinedQuizzes"] as? List<String> ?: listOf()

            )
        }
    }
}
