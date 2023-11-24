package com.tarren.personalquiznew.ui.student.studentDashboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tarren.personalquiznew.R

class StudentDashboardFragment : Fragment() {

    companion object {
        fun newInstance() = StudentDashboardFragment()
    }

    private lateinit var viewModel: StudentDashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_dashboard2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StudentDashboardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}