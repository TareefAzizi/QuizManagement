package com.tarren.personalquiznew.ui.student.questions

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
    private lateinit var countdownTimer: CountDownTimer  // Declare the countdownTimer here
    private lateinit var timerTextView: TextView  // Declare the TextView for the timer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quiz_questions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timerTextView = view.findViewById(R.id.timerTextView)

        setupRecyclerView(view)

        val quizId = QuizQuestionsFragmentArgs.fromBundle(requireArguments()).quizId
        viewModel.fetchQuizDetails(quizId)

        viewModel.timeLimit.observe(viewLifecycleOwner) { limit ->
            startCountdownTimer(limit) // limit is in minutes
        }

        viewModel.quizQuestions.observe(viewLifecycleOwner) { questions ->
            if (questions.isNotEmpty()) {
                questionsAdapter.submitList(questions)
            } else {
                Toast.makeText(context, "No questions available for this quiz", Toast.LENGTH_LONG).show()
            }
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

    private fun startCountdownTimer(minutes: Int) {
        val timeInMillis = minutes * 60 * 1000L // Convert minutes to milliseconds

        countdownTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Calculate minutes and seconds from millisUntilFinished
                val minutesLeft = (millisUntilFinished / 1000) / 60
                val secondsLeft = (millisUntilFinished / 1000) % 60

                // Format the string to have two digits for both minutes and seconds
                val formattedTime = String.format("%02d:%02d", minutesLeft, secondsLeft)
                timerTextView.text = formattedTime
            }

            override fun onFinish() {
                evaluateQuiz()
            }
        }.start()
    }




    private fun evaluateQuiz() {
        val userAnswers = questionsAdapter.getUserAnswers()
        val correctCount = viewModel.quizQuestions.value?.count { question ->
            userAnswers[question.questionId] == question.correctAnswer
        } ?: 0

        Toast.makeText(context, "You got $correctCount correct answers!", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::countdownTimer.isInitialized) {
            countdownTimer.cancel()
        }
    }

}
