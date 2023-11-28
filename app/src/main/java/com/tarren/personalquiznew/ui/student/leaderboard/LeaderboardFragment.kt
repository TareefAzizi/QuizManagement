package com.tarren.personalquiznew.ui.student.leaderboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.ui.adapter.LeaderboardAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LeaderboardFragment : Fragment() {

    // Using Hilt's by viewModels() delegation
    private val viewModel: LeaderboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.leaderboards.observe(viewLifecycleOwner) { leaderboards ->
            Log.d("LeaderboardFragment", "LiveData observer triggered with data: $leaderboards")
            setupRecyclerView(leaderboards)
        }
    }

    private fun setupRecyclerView(leaderboardData: Map<String, List<Pair<String, Int>>>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.leaderboardRecyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = LeaderboardAdapter(leaderboardData)
    }
}