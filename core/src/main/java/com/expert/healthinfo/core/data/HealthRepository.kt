package com.expert.healthinfo.core.data

import com.expert.healthinfo.core.data.source.local.LocalDataSource
import com.expert.healthinfo.core.data.source.remote.RemoteDataSource
import com.expert.healthinfo.core.data.source.remote.network.ApiResponse
import com.expert.healthinfo.core.data.source.remote.response.HeadlinesResponse
import com.expert.healthinfo.core.domain.model.Headlines
import com.expert.healthinfo.core.domain.repository.IheadlinesRepository
import com.expert.healthinfo.core.utils.AppExecutors
import com.expert.healthinfo.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HealthRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: com.expert.healthinfo.core.data.source.local.LocalDataSource,
    private val appExecutors: AppExecutors
) : IheadlinesRepository {

    override fun getAllHeadlines(): Flow<com.expert.healthinfo.core.data.Result<List<Headlines>>> =
        object : com.expert.healthinfo.core.data.NetworkBoundResource<List<Headlines>, List<HeadlinesResponse>>(appExecutors) {
            override fun loadFromNetwork(data: List<HeadlinesResponse>): Flow<List<Headlines>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<HeadlinesResponse>>> {
                return remoteDataSource.getAllTourism()
            }

        }.asFlow()

    override fun getFavoriteHeadlines(): Flow<List<Headlines>> {
        return localDataSource.getFavoriteHeadlines().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun isHeadlineFavorite(idHeadlines: String): Flow<Boolean> {
        return localDataSource.isHeadlineFavorite(idHeadlines)
    }

    override suspend fun insertFavoriteHeadlines(headlines: Headlines) {
        val domainHeadlines = DataMapper.mapDomainToEntity(headlines)
        return localDataSource.insertHeadlinesFav(domainHeadlines)
    }

    override suspend fun deleteFavoriteHeadlines(headlines: Headlines): Int {
        val domainHeadlines = DataMapper.mapDomainToEntity(headlines)
        return localDataSource.deleteHeadlinesFav(domainHeadlines)
    }

//    override fun setFavoriteHeadlines(headlines: Headlines, state: Boolean) {
//        val headlinesEntity = DataMapper.mapDomainToEntity(headlines)
//        appExecutors.diskIO().execute { localDataSource.setHeadlinesFavorite(headlinesEntity, state) }
//    }
}