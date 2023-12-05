package com.tarren.personalquiznew.ui.teacher.QuizManagement

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.data.model.Quiz
import com.tarren.personalquiznew.databinding.FragmentQuizManagementBinding
import com.tarren.personalquiznew.ui.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizManagementFragment : Fragment() {

    private val viewModel: QuizManagementViewModel by viewModels()
    private var _binding: FragmentQuizManagementBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var tvSelectedCsvFileName: TextView? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var selectedCsvUri: Uri? = null
    private var selectedCsvFileName: String? = null

    // Define an ActivityResultLauncher for file picking
    private val pickCsvFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedCsvUri = result.data?.data
            selectedCsvFileName = getFileName(selectedCsvUri)
            tvSelectedCsvFileName?.text = selectedCsvFileName ?: "No file selected"
            Toast.makeText(requireContext(), "CSV file selected: $selectedCsvFileName", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileName(fileUri: Uri?): String? {
        fileUri ?: return null
        var name: String? = null
        val cursor = requireContext().contentResolver.query(fileUri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        }
        return name
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
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_quiz, null)
        val alertDialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        val etQuizName = dialogView.findViewById<EditText>(R.id.etQuizName)
        val etQuizDescription = dialogView.findViewById<EditText>(R.id.etQuizDescription)
        tvSelectedCsvFileName = dialogView.findViewById<TextView>(R.id.tvSelectedCsvFileName)
        tvSelectedCsvFileName?.text = selectedCsvFileName ?: "No file selected"

        dialogView.findViewById<View>(R.id.btnSubmit).setOnClickListener {
            val quizName = etQuizName.text.toString()
            val quizDescription = etQuizDescription.text.toString()

            if (quizName.isBlank() || quizDescription.isBlank() || selectedCsvUri == null) {
                Toast.makeText(requireContext(), "Please provide quiz name, description, and select a CSV file.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
            val newQuiz = Quiz(name = quizName, description = quizDescription, teacherId = currentUserId)

            // Assuming createQuiz is a suspending function
            lifecycleScope.launch {
                val creationSuccess = viewModel.createQuiz(newQuiz, selectedCsvUri)
                if (creationSuccess) {
                    sharedViewModel.notifyQuizUpdated()
                    Toast.makeText(requireContext(), "Quiz uploaded successfully!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to upload quiz.", Toast.LENGTH_LONG).show()
                }
            }

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
        pickCsvFileLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
