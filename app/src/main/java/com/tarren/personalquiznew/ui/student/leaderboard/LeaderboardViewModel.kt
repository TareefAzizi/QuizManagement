package com.tarren.personalquiznew.ui.student.leaderboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tarren.personalquiznew.data.repo.QuizRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val quizRepo: QuizRepo
) : ViewModel() {

    val leaderboards = liveData {
        Log.d("LeaderboardViewModel", "Fetching leaderboards")
        val data = quizRepo.fetchAllQuizLeaderboards()
        Log.d("LeaderboardViewModel", "Data fetched: $data")
        emit(data)
    }

}
