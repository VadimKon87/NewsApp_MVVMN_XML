package com.androiddevs.mvvmnewsapp.api

import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    // API documentation - https://newsapi.org/docs/endpoints/top-headlines
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") // this is also part of how to make request, see API documentation
        countryCode: String = "ca", // set the default to Canada
        @Query("page")
        pageNumber: Int = 1, // get the first page of breaking news, and not all the news at once
        @Query("apiKey")
        apiKey: String = API_KEY
    ) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") // how to make search query, see api documentation
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ) : Response<NewsResponse>
}