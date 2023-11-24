package com.tarren.personalquiznew.ui.teacher.teacherDasboard

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.tarren.personalquiznew.R

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tarren.personalquiznew.data.model.Quiz
import com.tarren.personalquiznew.ui.adapter.QuizAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TeacherDashboardFragment : Fragment() {

    // Use viewModels delegate to get the ViewModel
    private val viewModel: TeacherDashboardViewModel by viewModels()

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
            recyclerView.adapter = QuizAdapter(quizzes) { quiz ->
                showEditQuizDialog(quiz)
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


}
