package com.quiz.app.model.response

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("answers") val answers: Answers,
    @SerializedName("correctAnswer") val correctAnswer: String,
    @SerializedName("question") val question: String,
    @SerializedName("questionImageUrl") val questionImageUrl: String,
    @SerializedName("score") val score: Int
)
