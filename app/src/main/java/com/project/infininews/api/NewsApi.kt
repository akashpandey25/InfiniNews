package com.project.infininews.api

import com.project.infininews.model.NewsResponse
import com.project.infininews.utils.Contents.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApi { //this is for headlines some queries
    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        countryCode: String="in",
        @Query("page")
        pageNumber: Int=1,
        @Query("apiKey")
        apiKey: String=API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything") //this for when user wants to search anything
    suspend fun searchNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int=1,
        @Query("apiKey")
        apiKey: String= API_KEY
    ):Response<NewsResponse>
}