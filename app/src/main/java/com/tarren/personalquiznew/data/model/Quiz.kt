package com.tarren.personalquiznew.data.model
data class Quiz(
    var quizId: String = "",
    val name: String = "",
    val description: String = "",
    val teacherId: String = "",
    val csvFileUrl: String = "",
    var timeLimit: Int = 0
) {
    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "quizId" to quizId,
            "name" to name,
            "description" to description,
            "teacherId" to teacherId,
            "csvFileUrl" to csvFileUrl,
            "timeLimit" to timeLimit
        )
    }
}

data class QuizQuestion(
    val questionId: String,
    val quizTitle: String,
    val question: String,
    val options: List<String>, // Changed to List<String> to store multiple options
    val correctAnswer: String
)

data class QuizAttempt(
    val userId: String,
    val userEmail: String, // Add this field for user's name
    val quizId: String,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val timestamp: Long = System.currentTimeMillis()
)






data class QuizResult(
    val studentId: String, // Identifier for the student who took the quiz
    val quizId: String, // Identifier for the quiz
    val answers: Map<String, String>, // Map of question IDs to student's answers
    val score: Int, // Total score chieved by the student
    val timeTaken: Int // Total time taken by the student to complete the quiz in seconds
)