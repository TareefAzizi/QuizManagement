//individual csvs
package com.tarren.personalquiznew.ui.adapter

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
    private var selectedAnswers: MutableMap<String, String> = mutableMapOf()
    private var userAnswers: MutableMap<String, String> = mutableMapOf()


    class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionText: TextView = view.findViewById(R.id.questionText)
        val optionsRadioGroup: RadioGroup = view.findViewById(R.id.optionsRadioGroup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quiz_question, parent, false)
        return QuestionViewHolder(view)
    }
    fun getUserAnswers(): Map<String, String> = userAnswers

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.questionText.text = question.question

        holder.optionsRadioGroup.removeAllViews()
        question.options.forEach { option ->
            val radioButton = RadioButton(holder.itemView.context).apply {
                text = option
                id = View.generateViewId()
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        userAnswers[question.questionId] = option
                    }
                }
            }
            holder.optionsRadioGroup.addView(radioButton)
        }
    }

    fun getSelectedAnswers(): Map<String, String> = selectedAnswers


    fun submitList(newQuestions: List<QuizQuestion>) {
        questions = newQuestions
        notifyDataSetChanged()
    }

    override fun getItemCount() = questions.size
}
