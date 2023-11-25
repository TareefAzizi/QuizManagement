package com.tarren.personalquiznew.ui.student.questions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.data.model.QuizQuestion

class StudentQuizQuestionsAdapter : RecyclerView.Adapter<StudentQuizQuestionsAdapter.QuestionViewHolder>() {

    private var questions: List<QuizQuestion> = emptyList()

    class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionText: TextView = view.findViewById(R.id.questionText)
        val optionsRadioGroup: RadioGroup = view.findViewById(R.id.optionsRadioGroup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quiz_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.questionText.text = question.question

        holder.optionsRadioGroup.removeAllViews() // Clear old options
        question.options.forEachIndexed { index, option ->
            val radioButton = RadioButton(holder.itemView.context).apply {
                text = option
                id = View.generateViewId()
            }
            holder.optionsRadioGroup.addView(radioButton)
        }
    }

    fun submitList(newQuestions: List<QuizQuestion>) {
        questions = newQuestions
        notifyDataSetChanged()
    }

    override fun getItemCount() = questions.size
}
