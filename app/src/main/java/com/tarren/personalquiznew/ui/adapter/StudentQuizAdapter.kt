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

class StudentQuizAdapter(private val quizzes: List<Quiz>, private val onTakeQuizClicked: (String) -> Unit)
    : RecyclerView.Adapter<StudentQuizAdapter.QuizViewHolder>() {

    class QuizViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quizTitle: TextView = view.findViewById(R.id.quizTitleTextView)
        val quizDescription: TextView = view.findViewById(R.id.quizDescriptionTextView)
        val takeQuizButton: Button = view.findViewById(R.id.takeQuizButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_quiz_item, parent, false)
        return QuizViewHolder(view)
    }


    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.quizTitle.text = quiz.name
        holder.quizDescription.text = quiz.description

        Log.d("StudentQuizAdapter", "Binding view for quiz: ${quiz.name}")

        holder.takeQuizButton.setOnClickListener {
            onTakeQuizClicked(quiz.quizId)
        }
    }


    override fun getItemCount() = quizzes.size
}