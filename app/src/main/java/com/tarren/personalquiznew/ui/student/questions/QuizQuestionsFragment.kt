package com.tarren.personalquiznew.ui.student.questions

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.data.model.QuizAttempt
import com.tarren.personalquiznew.ui.adapter.StudentQuizQuestionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizQuestionsFragment : Fragment() {

    private val viewModel: QuizQuestionsViewModel by viewModels()
    private lateinit var questionsAdapter: StudentQuizQuestionsAdapter
    private lateinit var countdownTimer: CountDownTimer
    private var timerTextView: TextView? = null
    private var isQuizEvaluated = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quiz_questions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = QuizQuestionsFragmentArgs.fromBundle(requireArguments())
        val isTeacher = args.isTeacher

        timerTextView = view.findViewById(R.id.timerTextView)

        viewModel.fetchQuizQuestions(args.quizId)


        if (isTeacher) {
            timerTextView?.visibility = View.GONE
            view.findViewById<Button>(R.id.submitQuizButton)?.visibility = View.GONE
        } else {
            setupCountdownTimer()
        }




        setupRecyclerView(view)

        val quizId = QuizQuestionsFragmentArgs.fromBundle(requireArguments()).quizId
        Log.d("QuizQuestionsFragment", "Received quizId: $quizId")

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
            findNavController().popBackStack()
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
                val minutesLeft = (millisUntilFinished / 1000) / 60
                val secondsLeft = (millisUntilFinished / 1000) % 60
                val formattedTime = String.format("%02d:%02d", minutesLeft, secondsLeft)
                timerTextView?.text = formattedTime // Use safe call here
            }

            override fun onFinish() {
                if (!isQuizEvaluated) {
                    evaluateQuiz()
                    findNavController().popBackStack()
                }
            }
        }.start()
    }






    private fun evaluateQuiz() {
        if (isQuizEvaluated) {
            return
        }
        isQuizEvaluated = true  // Set flag to true as quiz is now evaluated

        val userAnswers = questionsAdapter.getUserAnswers()
        val correctCount = viewModel.quizQuestions.value?.count { question ->
            userAnswers[question.questionId] == question.correctAnswer
        } ?: 0

        val totalQuestions = viewModel.quizQuestions.value?.size ?: 0
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val userId = firebaseUser?.uid ?: return
        val userEmail = firebaseUser?.email ?: "Unknown Email"

        val quizAttempt = QuizAttempt(
            userId = userId,
            userEmail = userEmail,
            quizId = QuizQuestionsFragmentArgs.fromBundle(requireArguments()).quizId,
            correctAnswers = correctCount,
            totalQuestions = totalQuestions
        )

        viewModel.saveQuizAttempt(quizAttempt)

        Toast.makeText(context, "You got $correctCount out of $totalQuestions correct answers!", Toast.LENGTH_LONG).show()
    }
    private fun setupCountdownTimer() {
        viewModel.timeLimit.observe(viewLifecycleOwner) { limit ->
            startCountdownTimer(limit) // limit is in minutes
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        if (this::countdownTimer.isInitialized) {
            countdownTimer.cancel()
        }
    }

}
