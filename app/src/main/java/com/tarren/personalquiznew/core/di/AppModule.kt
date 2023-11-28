package com.tarren.personalquiznew.core.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tarren.personalquiznew.data.repo.UserRepo
import com.tarren.personalquiznew.data.repo.UserRepoImpl
import com.tarren.personalquiznew.core.service.AuthService
import com.tarren.personalquiznew.core.service.StorageService
import com.tarren.personalquiznew.core.utils.NativeUtils
import com.tarren.personalquiznew.data.repo.QuizRepo
import com.tarren.personalquiznew.data.repo.QuizRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// This is the Dagger Hilt module used for providing dependencies for the application.
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    // Provides a singleton instance of AuthService for the application context.
    @Provides
    @Singleton
    fun provideAuth(@ApplicationContext context: Context): AuthService {
        return AuthService()
    }

    // Provides a singleton instance of StorageService.
    @Provides
    @Singleton
    fun provideStorageService(): StorageService {
        return StorageService()
    }

    // Provides a singleton DatabaseReference for Firebase Realtime Database.
    @Provides
    @Singleton
    fun provideFirebaseRealtimeRef(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("todos")
    }

    // Provides a singleton FirebaseFirestore instance.
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorageRef(): StorageReference {
        return FirebaseStorage.getInstance().reference
    }

    // Updated to provide UserRepoImpl with FirebaseFirestore and StorageReference.
    @Provides
    @Singleton
    fun provideUserRepoFirestore(db: FirebaseFirestore, storageRef: StorageReference): UserRepo {
        return UserRepoImpl(db, storageRef) // Now passing both Firestore and StorageReference
    }


    @Provides
    @Singleton
    fun provideQuizRepo(db: FirebaseFirestore, storageRef: StorageReference):  QuizRepo {
        return QuizRepoImpl(db, storageRef) // Pass FirebaseStorage
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }


    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }



}