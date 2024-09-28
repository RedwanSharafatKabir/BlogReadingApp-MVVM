package com.blog.app.network

import com.blog.app.model.data.BlogResponse
import com.blog.app.model.handleResponse.EndPoints
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET(EndPoints.EVERYTHING)
    suspend fun getBlogsList(
        @Query("q") q: String,
        @Query("apiKey") apiKey: String
    ): Response<BlogResponse>

}
