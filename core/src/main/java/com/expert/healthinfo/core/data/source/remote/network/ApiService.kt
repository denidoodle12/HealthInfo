package com.expert.healthinfo.core.data.source.remote.network

import com.expert.healthinfo.core.BuildConfig
import com.expert.healthinfo.core.data.source.remote.response.ListHeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    suspend fun getListHeadlines(
        @Query("q") q: String = "cancer",
        @Query("category") category: String = "health",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): ListHeadlinesResponse
}

