package com.blog.app.model.data

import com.google.gson.annotations.SerializedName

data class BlogResponse(
    @SerializedName("articles") val articles: List<Article>?,
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int?
)