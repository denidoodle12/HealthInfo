package com.expert.healthinfo.core.domain.usecase

import com.expert.healthinfo.core.data.Result
import com.expert.healthinfo.core.domain.model.Headlines
import kotlinx.coroutines.flow.Flow

interface HeadlinesUseCase {

    fun getAllHeadlines(): Flow<com.expert.healthinfo.core.data.Result<List<Headlines>>>

    fun getFavoriteHeadlines(): Flow<List<Headlines>>

    fun isHeadlineFavorite(idHeadlines: String): Flow<Boolean>

    suspend fun insertFavoriteHeadlines(headlines: Headlines)

    suspend fun deleteFavoriteHeadlines(headlines: Headlines) : Int

//    fun setFavoriteHeadlines(headlines: Headlines, state: Boolean)

}