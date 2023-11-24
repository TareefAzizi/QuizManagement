package com.tarren.personalquiznew.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo,
) : BaseViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = safeApiCall { authService.login(email, pass) }
            if (res == null) {
                _error.emit("Email or password is wrong")
            } else {
                // Assuming getUser() fetches the User object from the repository
                val userObj = userRepo.getUser(res.uid)
                _user.emit(userObj)
            }
        }
    }
}

