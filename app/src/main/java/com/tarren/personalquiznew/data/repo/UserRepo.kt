package com.tarren.personalquiznew.data.repo

import android.net.Uri
import com.tarren.personalquiznew.data.model.User

interface UserRepo {
    suspend fun addUser(id: String, user: User)
    suspend fun getUser(id: String): User?
    suspend fun updateUser(id: String, user: User)
}