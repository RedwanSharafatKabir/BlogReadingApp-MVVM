package com.blog.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blog.app.model.data.Article
import com.blog.app.model.data.BlogResponse
import com.blog.app.model.data.ErrorResponse
import com.blog.app.model.handleResponse.Event
import com.blog.app.model.repository.MainRepository
import com.google.gson.Gson
import com.blog.app.model.handleResponse.Resource
import com.blog.app.util.DateFormatter
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class BlogsViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository: MainRepository = MainRepository(application)

    private val _getBlogsResponse = MutableLiveData<Event<Resource<BlogResponse>>>()
    val getBlogsResponse: LiveData<Event<Resource<BlogResponse>>> = _getBlogsResponse

    fun getBlogsList(q: String, apiKey: String) = viewModelScope.launch {
        _getBlogsResponse.postValue(Event(Resource.Loading()))
        try {
            val response = appRepository.getBlogsList(q, apiKey)
            _getBlogsResponse.postValue(handleBlogResponse(response))

        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _getBlogsResponse.postValue(
                        Event(
                            Resource.Error(
                                "Request not succeed"
                            )
                        )
                    )
                }
                else -> {
                    _getBlogsResponse.postValue(
                        Event(
                            Resource.Error(
                                "Request not succeed"
                            )
                        )
                    )
                }
            }
        }
    }

    private fun handleBlogResponse(response: Response<BlogResponse>): Event<Resource<BlogResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return if (resultResponse.status.lowercase()=="ok") {
                    if(resultResponse.articles!!.isNotEmpty()){
                        val modifiedBlogList: MutableList<Article> = resultResponse.articles!!.map {
                            val formattedDate = DateFormatter.formattedDateTime(it.publishedAt!!)
                            Article(it.author, it.content, it.description, formattedDate, it.title, it.url, it.urlToImage)
                        }.toMutableList()

                        Event(Resource.Success(BlogResponse(modifiedBlogList, resultResponse.status, resultResponse.totalResults)))

                    } else {
                        Event(Resource.Error("No news articles published today."))
                    }

                } else {
                    val errorBody = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    Event(Resource.Error(errorBody.error))
                }
            }
        }

        return try {
            val errorBody = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
            Event(Resource.Error(errorBody.error))

        } catch (e: Exception) {
            Event(
                Resource.Error(
                    "Request not succeed"
                ))
        }
    }

}
