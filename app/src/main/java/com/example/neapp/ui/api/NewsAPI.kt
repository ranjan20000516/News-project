package com.example.neapp.ui.api

import com.example.neapp.ui.model.NewsResponse
import com.example.neapp.ui.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String= "in",
        @Query("page")
        pageNumber: Int= 1,
        @Query("apikey")
        apikey: String= API_KEY
        ):Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int= 1,
        @Query("apikey")
        apikey: String= API_KEY
    ):Response<NewsResponse>
}