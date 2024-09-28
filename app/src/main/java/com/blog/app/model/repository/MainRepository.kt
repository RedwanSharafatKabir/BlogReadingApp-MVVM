package com.blog.app.model.repository

import android.content.Context
import com.blog.app.network.APIClient
import com.blog.app.network.APIService

class MainRepository(ctx: Context) {

    private val apiService: APIService = APIClient.create(ctx)

    suspend fun getBlogsList(q: String, apiKey: String) = apiService.getBlogsList(q, apiKey)

}
