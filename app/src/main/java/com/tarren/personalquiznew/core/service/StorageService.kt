package com.tarren.personalquiznew.core.service

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

// This service manages file storage operations using Firebase Storage.
class StorageService(
    private val storage: StorageReference = FirebaseStorage.getInstance().reference
) {

}