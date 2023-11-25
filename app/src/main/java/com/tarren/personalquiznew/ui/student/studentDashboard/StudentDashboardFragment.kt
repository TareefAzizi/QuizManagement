package com.tarren.personalquiznew.ui.student.studentDashboard

    import android.app.AlertDialog
    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Button
    import android.widget.EditText
    import android.widget.Toast
    import androidx.fragment.app.viewModels
    import androidx.navigation.Navigation.findNavController
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.google.android.material.floatingactionbutton.FloatingActionButton
    import com.google.firebase.auth.FirebaseAuth
    import com.tarren.personalquiznew.NavGraphDirections
    import com.tarren.personalquiznew.R
    import com.tarren.personalquiznew.ui.adapter.StudentQuizAdapter
    import dagger.hilt.android.AndroidEntryPoint


    @AndroidEntryPoint
    class StudentDashboardFragment : Fragment() {

        companion object {
            fun newInstance() = StudentDashboardFragment()
        }

        private val viewModel: StudentDashboardViewModel by viewModels()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_student_dashboard2, container, false)
        }



        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Initialize RecyclerView
            val recyclerView = view.findViewById<RecyclerView>(R.id.joinedQuizzesRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context)

            // Observe the joined quizzes LiveData
            viewModel.joinedQuizzes.observe(viewLifecycleOwner) { quizzes ->
                recyclerView.adapter = StudentQuizAdapter(quizzes) { quizId ->
                    takeQuiz(quizId)
                }

            }


            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            if (currentUserId != null) {
                viewModel.fetchJoinedQuizzes(currentUserId)
            }


            view.findViewById<FloatingActionButton>(R.id.joinQuizButton)?.setOnClickListener {
                showJoinQuizDialog()
            }
        }


        private fun showJoinQuizDialog() {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_join_quiz, null)
            val quizIdEditText = dialogView.findViewById<EditText>(R.id.quizIdEditText)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
                .create()

            dialogView.findViewById<Button>(R.id.submitQuizIdButton).setOnClickListener {
                val quizId = quizIdEditText.text.toString()
                joinQuiz(quizId)
                dialog.dismiss() // Dismiss the dialog after submitting
            }

            dialog.show()
        }


        private fun joinQuiz(quizId: String) {
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

            if (currentUserId != null) {
                viewModel.joinQuiz(currentUserId, quizId, onSuccess = {
                    // Toast message for successful joining
                    Toast.makeText(context, "Successfully joined quiz: $quizId", Toast.LENGTH_SHORT).show()

                    // Re-fetch the quizzes to update the list
                    viewModel.fetchJoinedQuizzes(currentUserId)
                }, onError = { errorMessage ->
                    // Toast message for error
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }



        private fun takeQuiz(quizId: String) {
            try {
                val action = NavGraphDirections.globalActionToQuizQuestionsFragment(quizId)
                findNavController().navigate(action)
            } catch (e: Exception) {
                // Handle any navigation errors
                e.printStackTrace()
            }
        }





    }



