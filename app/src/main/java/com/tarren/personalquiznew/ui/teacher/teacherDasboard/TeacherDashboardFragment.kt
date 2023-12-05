package com.tarren.personalquiznew.ui.teacher.teacherDasboard

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tarren.personalquiznew.NavGraphDirections
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.data.model.Quiz
import com.tarren.personalquiznew.ui.SharedViewModel
import com.tarren.personalquiznew.ui.adapter.QuizQuestionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherDashboardFragment : Fragment() {

    // Use viewModels delegate to get the ViewModel
    private val viewModel: TeacherDashboardViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_teacher_dashboard2, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.quizzesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.quizzes.observe(viewLifecycleOwner) { quizzes ->
            recyclerView.adapter = QuizQuestionsAdapter(
                quizzes,
                onQuizItemClicked = { quizId ->
                    navigateToQuizDetails(quizId)
                },
                onEditQuizClicked = { quiz -> showEditQuizDialog(quiz) },
                onDeleteClicked = { quizId -> confirmAndDeleteQuiz(quizId) }
            )
        }

        sharedViewModel.quizUpdated.observe(viewLifecycleOwner) { updated ->
            if (updated) {
                viewModel.fetchQuizzes() // Fetch or refresh quizzes
                sharedViewModel.notifyQuizUpdated(false) // Reset the flag
            }
        }
    }



    private fun showEditQuizDialog(quiz: Quiz) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_quiz_time, null)
        val etTimeLimit = dialogView.findViewById<EditText>(R.id.etTimeLimit)
        etTimeLimit.setText(quiz.timeLimit.toString())

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newTimeLimit = etTimeLimit.text.toString().toIntOrNull() ?: quiz.timeLimit
                val updatedQuiz = quiz.copy(timeLimit = newTimeLimit)
                viewModel.updateQuizTime(updatedQuiz)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmAndDeleteQuiz(quizId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this quiz?")
            .setPositiveButton("Delete") { _, _ -> deleteQuiz(quizId) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteQuiz(quizId: String) {
        viewModel.deleteQuiz(quizId)
    }

    private fun navigateToQuizDetails(quizId: String) {
        try {
            val action = NavGraphDirections.globalActionToQuizQuestionsFragment(quizId, isTeacher = true)
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e("TeacherDashboard", "Navigation error: ${e.message}", e)
        }
    }









}


