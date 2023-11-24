package com.tarren.personalquiznew.ui.student.studentDashboard

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.tarren.personalquiznew.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StudentDashboardFragment : Fragment() {

    companion object {
        fun newInstance() = StudentDashboardFragment()
    }

    private val viewModel: StudentDashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_dashboard2, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<FloatingActionButton>(R.id.joinQuizButton)?.setOnClickListener {
            showJoinQuizDialog()
        }
    }


    private fun showJoinQuizDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_join_quiz, null)
        val quizIdEditText = dialogView.findViewById<EditText>(R.id.quizIdEditText)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialogView.findViewById<Button>(R.id.submitQuizIdButton).setOnClickListener {
            val quizId = quizIdEditText.text.toString()
            joinQuiz(quizId)
            dialog.dismiss() // Dismiss the dialog after submitting
        }

        dialog.show()
    }


    private fun joinQuiz(quizId: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserId != null) {
            viewModel.joinQuiz(currentUserId, quizId, onSuccess = {
                Toast.makeText(context, "Successfully joined quiz: $quizId", Toast.LENGTH_SHORT).show()
            }, onError = { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            })
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }


}