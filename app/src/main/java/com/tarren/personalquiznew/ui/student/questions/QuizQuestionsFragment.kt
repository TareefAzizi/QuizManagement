package com.tarren.personalquiznew.ui.student.questions

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.ui.adapter.StudentQuizQuestionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizQuestionsFragment : Fragment() {

    private val viewModel: QuizQuestionsViewModel by viewModels()
    private lateinit var questionsAdapter: StudentQuizQuestionsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quiz_questions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)

        // Get the quiz ID from the fragment arguments
        val quizId = QuizQuestionsFragmentArgs.fromBundle(requireArguments()).quizId
        viewModel.fetchQuizQuestions(quizId)

        viewModel.quizQuestions.observe(viewLifecycleOwner) { questions ->
            Log.d("QuizQuestionsFragment", "Questions received: ${questions.size}")
            questionsAdapter.submitList(questions)
        }

        val submitButton = view.findViewById<Button>(R.id.submitQuizButton)
        submitButton.setOnClickListener {
            evaluateQuiz()
        }
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.quizQuestionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        questionsAdapter = StudentQuizQuestionsAdapter() // Change to your actual adapter name
        recyclerView.adapter = questionsAdapter
    }

    private fun evaluateQuiz() {
        val userAnswers = questionsAdapter.getUserAnswers()
        val correctCount = viewModel.quizQuestions.value?.count { question ->
            userAnswers[question.questionId] == question.correctAnswer
        } ?: 0

        Toast.makeText(context, "You got $correctCount correct answers!", Toast.LENGTH_LONG).show()
    }
}
