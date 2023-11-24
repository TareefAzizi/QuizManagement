package com.tarren.personalquiznew.ui.teacher.teacherDasboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tarren.personalquiznew.R

class TeacherDashboardFragment : Fragment() {

    companion object {
        fun newInstance() = TeacherDashboardFragment()
    }

    private lateinit var viewModel: TeacherDashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_teacher_dashboard2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TeacherDashboardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}