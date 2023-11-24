package com.tarren.personalquiznew.ui.teacher.QuizManagement

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.data.model.Quiz
import com.tarren.personalquiznew.databinding.FragmentQuizManagementBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizManagementFragment : Fragment() {

    private val viewModel: QuizManagementViewModel by viewModels()
    private var _binding: FragmentQuizManagementBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var selectedCsvUri: Uri? = null

    // Define an ActivityResultLauncher for file picking
    private val pickCsvFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedCsvUri = result.data?.data
            // You might want to update your UI to show that a file has been selected
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.openCreateQuizDialogButton.setOnClickListener {
            showCreateQuizDialog()
        }
    }

    private fun showCreateQuizDialog() {
        // Inflate the dialog with custom view
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_quiz, null)
        val alertDialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        val etQuizName = dialogView.findViewById<EditText>(R.id.etQuizName)
        val etQuizDescription = dialogView.findViewById<EditText>(R.id.etQuizDescription)

        dialogView.findViewById<View>(R.id.btnSubmit).setOnClickListener {
            val quizName = etQuizName.text.toString()
            val quizDescription = etQuizDescription.text.toString()

            // Retrieve the current user's ID
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

            // Create a new Quiz object with the teacher's ID
            val newQuiz = Quiz(name = quizName, description = quizDescription, teacherId = currentUserId)
            viewModel.createQuiz(newQuiz, selectedCsvUri)

            alertDialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.btnSelectCsv).setOnClickListener {
            openFilePicker()
        }

        alertDialog.show()
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        pickCsvFileLauncher.launch(intent) // Use the launcher to start the activity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
