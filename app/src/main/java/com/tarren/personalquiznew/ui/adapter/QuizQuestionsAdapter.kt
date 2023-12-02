//for teacher dashboard
package com.tarren.personalquiznew.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.data.model.Quiz

class QuizQuestionsAdapter(
    private val quizzes: List<Quiz>,
    private val onQuizItemClicked: (String) -> Unit,
    private val onEditQuizClicked: (Quiz) -> Unit,
    private val onDeleteClicked: (String) -> Unit,
) : RecyclerView.Adapter<QuizQuestionsAdapter.QuizViewHolder>() {
    class QuizViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quizTitle: TextView = view.findViewById(R.id.quizTitleTextView)
        val quizId: TextView = view.findViewById(R.id.quizIdTextView)
        val quizTimeLimit: TextView = view.findViewById(R.id.quizTimeLimitTextView)
        val editQuizButton: Button = view.findViewById(R.id.editQuizButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quiz_item, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.quizTitle.text = quiz.name
        holder.quizId.text = quiz.quizId
        holder.quizTimeLimit.text = "Time Limit: ${quiz.timeLimit} minutes"


        holder.quizId.setOnClickListener {
            copyToClipboard(holder.quizId.context, quiz.quizId)
        }

        holder.editQuizButton.setOnClickListener {
            onEditQuizClicked(quiz)
        }

        holder.itemView.findViewById<ImageView>(R.id.deleteQuizButton).setOnClickListener {
            onDeleteClicked(quiz.quizId)
        }

        holder.itemView.setOnClickListener {
            onQuizItemClicked(quiz.quizId) // Trigger this when a quiz item is clicked
        }

    }

    private fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Quiz ID", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Quiz ID copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount() = quizzes.size
}
