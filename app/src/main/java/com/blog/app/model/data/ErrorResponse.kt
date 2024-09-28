package com.blog.app.model.data

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error") val error: String
)
