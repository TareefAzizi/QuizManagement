package com.tarren.personalquiznew.ui.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.databinding.FragmentRegisterBinding
import com.tarren.personalquiznew.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        setupUIComponents()
        setupViewModelObserver()
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()

        binding.run {
            btnRegister.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()
                val role = when (rgRole.checkedRadioButtonId) {
                    R.id.rbStudent -> "Student"
                    R.id.rbTeacher -> "Teacher"
                    else -> ""
                }
                if (confirmPassword != password) {
                    // Show error message for password mismatch
                } else {
                    viewModel.register(name, email, password, confirmPassword, role)
                }
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.registrationSuccess.collect { isSuccess ->
                if (isSuccess) {
                    val loginAction = RegisterFragmentDirections.actionRegisterToLogin()
                    navController.navigate(loginAction)
                    // Handle onSuccess event here, e.g., show a toast
                }
            }
        }
    }
}

