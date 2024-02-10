package com.quiz.app.repository

import android.content.Context
import com.quiz.app.retrofit.APIService
import com.quiz.app.retrofit.APIConfig

class MainRepository(ctx: Context) {

    private val apiService:APIService = APIConfig.create(ctx)

    fun getQuestions() = apiService.getQuestions()
}
