package com.tarren.personalquiznew.ui.tabContainer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.tarren.personalquiznew.R
import com.tarren.personalquiznew.data.repo.UserRepo
import com.tarren.personalquiznew.databinding.FragmentTabContainerBinding
import com.tarren.personalquiznew.ui.adapter.FragmentAdapter
import com.tarren.personalquiznew.ui.profile.ProfileFragment
import com.tarren.personalquiznew.ui.student.studentDashboard.StudentDashboardFragment
import com.tarren.personalquiznew.ui.teacher.teacherDasboard.TeacherDashboardFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class TabContainerFragment : Fragment() {
    // Binding object for accessing views in the layout.
    private lateinit var binding: FragmentTabContainerBinding

    // Injecting an instance of UserRepo to handle user-related data operations.
    @Inject
    lateinit var userRepo: UserRepo

    // Called to have the fragment instantiate its user interface view.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflating the layout for this fragment and initializing the binding.
        binding = FragmentTabContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Called immediately after onCreateView() has returned, but before any saved state has been restored into the view.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetching the current user's ID from Firebase Authentication.
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        // If a user ID is present, fetch user data and set up tabs accordingly.
        if (currentUserId != null) {
            fetchAndSetupUserTabs(currentUserId)
        }
    }

    // Function to fetch user data based on their ID and setup tabs in the ViewPager.
    private fun fetchAndSetupUserTabs(userId: String) {
        lifecycleScope.launch {
            Log.d("TabContainerFragment", "Fetching user data for user ID: $userId")
            val user = userRepo.getUser(userId)
            Log.d("TabContainerFragment", "User data fetched: $user")

            withContext(Dispatchers.Main) {
                val fragments = when (user?.role) {
                    "Student" -> listOf(StudentDashboardFragment(), ProfileFragment())
                    "Teacher" -> listOf(TeacherDashboardFragment(), ProfileFragment())
                    else -> listOf(StudentDashboardFragment(), ProfileFragment())
                }

                setupViewPagerWithTabs(fragments)
            }
        }
    }


    // Function to set up the ViewPager2 and TabLayout with the given fragments.
    private fun setupViewPagerWithTabs(fragments: List<Fragment>) {
        val fragmentAdapter = FragmentAdapter(this, fragments)
        binding.vpContainer.adapter = fragmentAdapter

        TabLayoutMediator(binding.tlTabs, binding.vpContainer) { tab, position ->
            tab.text = getTabTitle(position, fragments)
        }.attach()

        for (i in 0 until binding.tlTabs.tabCount) {
            val tab = binding.tlTabs.getTabAt(i)
            val tabView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null)
            val textView = tabView.findViewById<TextView>(R.id.customTabTextView)
            textView.text = getTabTitle(i, fragments)
            tab?.customView = tabView
        }
    }

    // Function to determine tab titles based on the position and the type of fragments
    private fun getTabTitle(position: Int, fragments: List<Fragment>): String {
        return when {
            fragments[position] is StudentDashboardFragment -> "Student Dashboard"
            fragments[position] is TeacherDashboardFragment -> "Teacher Dashboard"
            fragments[position] is ProfileFragment -> "Profile"
            else -> "Tab $position" // Fallback title for any unidentified fragments
        }
    }


}