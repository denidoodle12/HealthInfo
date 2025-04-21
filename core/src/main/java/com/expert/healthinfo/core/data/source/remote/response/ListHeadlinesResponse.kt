package com.expert.healthinfo.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListHeadlinesResponse (
    @field:SerializedName("totalResults")
    val totalResults: Int?,

    @field:SerializedName("articles")
    val articles: List<HeadlinesResponse>,

    @field:SerializedName("status")
    val status: String?
)
