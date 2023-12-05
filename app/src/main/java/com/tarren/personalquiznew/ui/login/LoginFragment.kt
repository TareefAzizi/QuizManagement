package com.tarren.personalquiznew.ui.login

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.data.model.User
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
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("LoginFragment", "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModel.signInWithGoogle(credential)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { user ->
                    if (user != null) {
                        Log.d("LoginFragment", "User role: ${user.role}")
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showRoleSelection.collect { newUser ->
                    newUser?.let { showRoleSelectionDialog(it) }
                }
            }
        }
    }    private fun showRoleSelectionDialog(user: User) {
        val roles = arrayOf("Teacher", "Student")
        AlertDialog.Builder(requireContext())
            .setTitle("Select your role")
            .setItems(roles) { _, which ->
                user.role = roles[which]
                viewModel.saveUserRole(user)
            }
            .show()
    }
    }




