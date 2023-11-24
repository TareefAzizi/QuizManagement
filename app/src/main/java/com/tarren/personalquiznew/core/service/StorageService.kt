package com.tarren.personalquiznew.core.service

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

// This service manages file storage operations using Firebase Storage.
class StorageService(
    private val storage: StorageReference = FirebaseStorage.getInstance().reference
) {
    // Uploads an image to Firebase Storage.
    suspend fun addImage(name: String, uri: Uri) {
        storage.child(name).putFile(uri).await()
    }

    // Retrieves the download URL of an image from Firebase Storage.
    suspend fun getImage(name: String): Uri? {
        return try {
            storage.child(name).downloadUrl.await()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}