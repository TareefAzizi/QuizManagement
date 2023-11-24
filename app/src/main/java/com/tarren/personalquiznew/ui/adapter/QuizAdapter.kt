package com.tarren.personalquiznew.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.data.model.Quiz

class QuizAdapter(private val quizzes: List<Quiz>) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    class QuizViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quizTitle: TextView = view.findViewById(R.id.quizTitleTextView)
        val quizId: TextView = view.findViewById(R.id.quizIdTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quiz_item, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.quizTitle.text = quiz.name
        holder.quizId.text = quiz.quizId

        holder.quizId.setOnClickListener {
            copyToClipboard(holder.quizId.context, quiz.quizId)
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
