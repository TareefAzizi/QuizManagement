package com.tarren.personalquiznew.data.model

import com.google.firebase.firestore.FirebaseFirestore

data class User(
    val id: String? = null,
    val name: String = "",
    val email: String = "",
    var role: String = "",
    val joinedQuizzes: List<String> = listOf()

) {
    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "role" to role,
            "joinedQuizzes" to joinedQuizzes
        )
    }

}
