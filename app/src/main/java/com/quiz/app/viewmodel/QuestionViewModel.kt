package com.quiz.app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.quiz.app.model.response.QuestionResponse
import com.quiz.app.repository.MainRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class QuestionViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository: MainRepository = MainRepository(application)
    val quesResponse = MutableLiveData<QuestionResponse>()
    val errorMessage = MutableLiveData<String>()

    fun getQuestionsMethod() = viewModelScope.launch {
        try{
            val call = appRepository.getQuestions()

            call.enqueue(object: Callback<QuestionResponse> {
                override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {
                    quesResponse.postValue(response.body())
                }

                override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })

        } catch (e: Exception){
            Log.i("http_exception", e.message.toString())
        }
    }
}
