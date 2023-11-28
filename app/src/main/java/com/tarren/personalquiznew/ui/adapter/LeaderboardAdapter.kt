package com.tarren.personalquiznew.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tarren.personalquiznew.R

class LeaderboardAdapter(private val leaderboardData: Map<String, List<Pair<String, Int>>>)
    : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    class LeaderboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quizNameTextView: TextView = view.findViewById(R.id.quizNameTextView)
        val studentScoreTextView: TextView = view.findViewById(R.id.studentScoreTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return LeaderboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val quizName = leaderboardData.keys.elementAt(position)
        val scores = leaderboardData[quizName]

        Log.d("LeaderboardAdapter", "Binding data for quiz: $quizName with scores: $scores")

        holder.quizNameTextView.text = quizName
        holder.studentScoreTextView.text = scores?.joinToString("\n") { "${it.first}: ${it.second}" }
    }


    override fun getItemCount(): Int {
        return leaderboardData.size
    }
}
