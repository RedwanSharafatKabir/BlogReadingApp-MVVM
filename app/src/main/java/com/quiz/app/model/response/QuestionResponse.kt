package com.quiz.app.model.response

import com.google.gson.annotations.SerializedName

data class QuestionResponse(
    @SerializedName("questions") val questions: List<Question>
)
