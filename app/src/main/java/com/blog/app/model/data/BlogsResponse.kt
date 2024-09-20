package com.blog.app.model.data

import com.google.gson.annotations.SerializedName

data class BlogsResponse(
    @SerializedName("title") val title: String
)
