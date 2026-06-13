package com.expert.healthinfo.core.data.source.local

import com.expert.healthinfo.core.data.source.local.entity.HeadlinesEntity
import com.expert.healthinfo.core.data.source.local.room.HeadlinesDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val headlinesDao: HeadlinesDao) {

    fun getAllHeadlines(): Flow<List<HeadlinesEntity>> = headlinesDao.getAllHeadlines()

    fun isHeadlineFavorite(idHeadlines: String): Flow<Boolean> {
        return headlinesDao.isHeadlineFavorite(idHeadlines)
    }

    fun getFavoriteHeadlines(): Flow<List<HeadlinesEntity>> = headlinesDao.getFavoriteHeadlines()

    suspend fun insertHeadlinesFav(headlinesList: HeadlinesEntity) = headlinesDao.insertHeadlines(headlinesList)

    suspend fun deleteHeadlinesFav(headlinesList: HeadlinesEntity) = headlinesDao.deleteFavoriteHeadlines(headlinesList)
}