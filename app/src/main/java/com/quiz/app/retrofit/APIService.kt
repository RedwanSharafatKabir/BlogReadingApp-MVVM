package com.quiz.app.retrofit

import com.quiz.app.model.EndPoints
import com.quiz.app.model.response.QuestionResponse
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @GET(EndPoints.QUIZ_JSON)
    fun getQuestions(): Call<QuestionResponse>
}
