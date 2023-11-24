package com.tarren.personalquiznew.ui.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarren.personalquiznew.core.service.AuthService
import com.tarren.personalquiznew.data.model.User
import com.tarren.personalquiznew.data.repo.UserRepo
import com.tarren.personalquiznew.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _registrationSuccess = MutableStateFlow<Boolean>(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess

    fun register(name: String, email: String, pass: String, confirmPass: String, role: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val error = validate(email, pass, confirmPass)
            if (error.isNotEmpty()) {
                _error.emit(error)
            } else {
                val res = safeApiCall { authService.register(email, pass) }
                if (res == null) {
                    _error.emit("Could not create user")
                } else {
                    val newUser = User(id = res.uid, name = name, email = res.email ?: "", role = role)
                    userRepo.addUser(res.uid, newUser)
                    _user.emit(newUser)
                    _registrationSuccess.emit(true)
                }
            }
        }
    }

    private fun validate(email: String, pass: String, confirmPass: String): String {
        return when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please provide a valid email address"
            pass.length < 6 -> "Password length must be greater than 5"
            pass != confirmPass -> "Password and confirm password are not the same"
            else -> ""
        }
    }
}
