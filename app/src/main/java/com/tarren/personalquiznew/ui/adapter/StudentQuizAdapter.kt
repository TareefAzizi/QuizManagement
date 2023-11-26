//for the dashboard  fragment
package com.tarren.personalquiznew.ui.adapter

import android.widget.Button
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.data.model.Quiz

class StudentQuizAdapter(
    private var quizzes: MutableList<Quiz>, // Changed to MutableList and private
    private val onTakeQuizClicked: (String) -> Unit)
    : RecyclerView.Adapter<StudentQuizAdapter.QuizViewHolder>() {
    fun updateQuizzes(newQuizzes: List<Quiz>) {
        quizzes.clear()
        quizzes.addAll(newQuizzes)
        notifyDataSetChanged()
    }
    class QuizViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quizTitle: TextView = view.findViewById(R.id.quizTitleTextView)
        val quizDescription: TextView = view.findViewById(R.id.quizDescriptionTextView)
        val takeQuizButton: Button = view.findViewById(R.id.takeQuizButton)
        val quizResultsTextView: TextView = view.findViewById(R.id.quizResultsTextView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_quiz_item, parent, false)
        return QuizViewHolder(view)
    }


    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.quizTitle.text = quiz.name
        holder.quizDescription.text = quiz.description

        if (quiz.isTaken) {
            holder.takeQuizButton.visibility = View.GONE
            holder.quizResultsTextView.visibility = View.VISIBLE
            holder.quizResultsTextView.text = "Score: ${quiz.correctAnswers}/${quiz.totalQuestions}"
        } else {
            holder.takeQuizButton.visibility = View.VISIBLE
            holder.quizResultsTextView.visibility = View.GONE
        }

        holder.takeQuizButton.setOnClickListener {
            if (!quiz.isTaken) {
                onTakeQuizClicked(quiz.quizId)
            }
        }
    }



    override fun getItemCount() = quizzes.size
}