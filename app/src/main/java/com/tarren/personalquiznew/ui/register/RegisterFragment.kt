package com.tarren.personalquiznew.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()
                val role = when (rgRole.checkedRadioButtonId) {
                    R.id.rbStudent -> "Student"
                    R.id.rbTeacher -> "Teacher"
                    else -> ""
                }

                // Check if any of the fields are empty
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role.isEmpty()) {
                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Check if passwords match
                if (confirmPassword != password) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    etConfirmPassword.error = "Passwords do not match"
                    return@setOnClickListener
                }

                // If all checks pass, proceed with registration
                viewModel.register(name, email, password, confirmPassword, role)
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

