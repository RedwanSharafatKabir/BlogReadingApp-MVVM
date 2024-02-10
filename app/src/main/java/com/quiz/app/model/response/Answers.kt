package com.quiz.app.model.response

import com.google.gson.annotations.SerializedName

data class Answers(
    @SerializedName("A") val A: String,
    @SerializedName("B") val B: String,
    @SerializedName("C") val C: String,
    @SerializedName("D") val D: String
)
