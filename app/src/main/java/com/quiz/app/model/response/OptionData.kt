package com.quiz.app.model.response

import com.google.gson.annotations.SerializedName

data class OptionData(
    @SerializedName("option") val option: String,
    @SerializedName("result") val result: String
)
