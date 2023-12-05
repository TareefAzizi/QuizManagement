package com.tarren.personalquiznew.data.model
data class Quiz(
    var quizId: String = "",
    val name: String = "",
    val description: String = "",
    val teacherId: String = "",
    val csvFileUrl: String = "",
    var timeLimit: Int = 1,
    var isTaken: Boolean = false,
    var correctAnswers: Int = 0,
    var totalQuestions: Int = 0
) {
    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "quizId" to quizId,
            "name" to name,
            "description" to description,
            "teacherId" to teacherId,
            "csvFileUrl" to csvFileUrl,
            "timeLimit" to timeLimit,
            "isTaken" to isTaken,
            "correctAnswers" to correctAnswers,
            "totalQuestions" to totalQuestions
        )
    }
}

data class QuizQuestion(
    val questionId: String,
    val quizTitle: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)

data class QuizAttempt(
    val userId: String = "",
    val userEmail: String = "",
    val quizId: String = "",
    val correctAnswers: Int = 0,
    val totalQuestions: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", 0, 0, System.currentTimeMillis())
}






data class QuizResult(
    val studentId: String,
    val quizId: String,
    val answers: Map<String, String>,
    val score: Int,
    val timeTaken: Int
)