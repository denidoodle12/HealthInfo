package com.expert.healthinfo.core.domain.usecase

import com.expert.healthinfo.core.data.Result
import com.expert.healthinfo.core.domain.model.Headlines
import com.expert.healthinfo.core.domain.repository.IheadlinesRepository
import kotlinx.coroutines.flow.Flow

class HeadlinesInteractor(private val headlinesRepository: IheadlinesRepository) : HeadlinesUseCase {

    override fun getAllHeadlines(): Flow<com.expert.healthinfo.core.data.Result<List<Headlines>>> {
        return headlinesRepository.getAllHeadlines()
    }

    override fun getFavoriteHeadlines(): Flow<List<Headlines>> {
        return headlinesRepository.getFavoriteHeadlines()
    }

    override fun isHeadlineFavorite(idHeadlines: String): Flow<Boolean> {
        return headlinesRepository.isHeadlineFavorite(idHeadlines)
    }

    override suspend fun insertFavoriteHeadlines(headlines: Headlines) {
        return headlinesRepository.insertFavoriteHeadlines(headlines)
    }

    override suspend fun deleteFavoriteHeadlines(headlines: Headlines): Int {
        return headlinesRepository.deleteFavoriteHeadlines(headlines)
    }

//    override fun setFavoriteHeadlines(headlines: Headlines, state: Boolean) {
//        return headlinesRepository.setFavoriteHeadlines(headlines, state)
//    }
}