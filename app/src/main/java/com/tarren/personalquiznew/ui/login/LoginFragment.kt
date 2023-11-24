package com.tarren.personalquiznew.ui.login

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.databinding.FragmentLoginBinding
import com.tarren.personalquiznew.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val viewModel: LoginViewModel by viewModels()
    private lateinit var signInClient: GoogleSignInClient

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val acc = task.result!!
                Log.d("debugging", "Google account signed in: ${acc.displayName}")
                // Here, you might want to handle successful Google Sign-In
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("debugging", "Google Sign-In failed: ${e.message}")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupUIComponents()
        return binding.root
    }

    override fun setupUIComponents() {
        // Google SignIn setup
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        signInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.apply {
            btnGoogleSignIn.setOnClickListener {
                googleSignInLauncher.launch(signInClient.signInIntent)
            }

            btnContinue.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                viewModel.login(email, password)
            }

            tvRegister.setOnClickListener {
                // Navigate to Register Fragment
                navController.navigate(R.id.action_loginFragment_to_registerFragment)
            }

            tvForgotPassword.setOnClickListener {
                // Handle forgot password

            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                if (user != null) {
                    val action = when (user.role) {
                        "Student" -> LoginFragmentDirections.toHome()
                        "Teacher" -> LoginFragmentDirections.toHome()
                        else -> null
                    }
                    action?.let { navController.navigate(it) }
                }
            }
        }
    }
}