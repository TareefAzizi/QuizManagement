package com.tarren.personalquiznew.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.tarren.personalquiznew.data.model.User
import com.tarren.personalquiznew.data.repo.UserRepo
import com.tarren.personalquiznew.core.service.AuthService
import com.tarren.personalquiznew.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo,
) : BaseViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    private val _showRoleSelection = MutableStateFlow<User?>(null)
    val showRoleSelection: StateFlow<User?> = _showRoleSelection

    fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = safeApiCall { authService.login(email, pass) }
            if (res == null) {
                _error.emit("Email or password is wrong")
                Log.d("LoginViewModel", "Login failed: Email or password is wrong")
            } else {
                val userObj = userRepo.getUser(res.uid)
                Log.d("LoginViewModel", "Login successful - User role: ${userObj?.role}")
                _user.emit(userObj)
            }
        }
    }

    fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val authResult = authService.signInWithCredential(credential)
                val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
                val firebaseUser = authResult.user

                if (isNewUser) {
                    // Create a new User object with the UID from Firebase Authentication
                    val newUser = User(
                        id = firebaseUser?.uid,
                        email = firebaseUser?.email ?: "",
                        name = firebaseUser?.displayName ?: ""
                    )
                    _showRoleSelection.emit(newUser)
                } else {
                    // For existing users, fetch the user data from Firestore using the UID
                    _user.emit(userRepo.getUser(firebaseUser?.uid!!))
                }
            } catch (e: Exception) {
                _error.emit("Authentication failed: ${e.message}")
            }
        }
    }

    fun saveUserRole(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.addOrUpdateUser(user)
            _user.emit(user)
        }
    }




}


