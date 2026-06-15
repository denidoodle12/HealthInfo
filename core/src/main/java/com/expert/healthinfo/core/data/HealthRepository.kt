package com.expert.healthinfo.core.data

import com.expert.healthinfo.core.data.source.local.LocalDataSource
import com.expert.healthinfo.core.data.source.remote.RemoteDataSource
import com.expert.healthinfo.core.data.source.remote.network.ApiResponse
import com.expert.healthinfo.core.data.source.remote.response.HeadlinesResponse
import com.expert.healthinfo.core.domain.model.Headlines
import com.expert.healthinfo.core.domain.repository.IheadlinesRepository
import com.expert.healthinfo.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class HealthRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IheadlinesRepository {

    override fun getAllHeadlines(query: String): Flow<Result<List<Headlines>>> {
        // API Flow: fetch dari network dengan query keyword
        val apiFlow = object : NetworkBoundResource<List<Headlines>, List<HeadlinesResponse>>() {
            override fun loadFromNetwork(data: List<HeadlinesResponse>): Flow<List<Headlines>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<HeadlinesResponse>>> {
                return remoteDataSource.getAllHeadlines(query)
            }
        }.asFlow()

        // Combine API flow dengan favorites dari Room secara reaktif.
        // Ketika user add/remove favorite, Room emit data baru →
        // combine() re-emit → isFavorite di daftar utama otomatis terupdate.
        return combine(apiFlow, localDataSource.getFavoriteHeadlines()) { result, favorites ->
            when (result) {
                is Result.Success -> {
                    val favoriteIds = favorites.map { it.idHeadlines }.toSet()
                    val updatedList = result.data?.map { headline ->
                        headline.copy(isFavorite = headline.idHeadlines in favoriteIds)
                    } ?: emptyList()
                    Result.Success(updatedList)
                }
                else -> result
            }
        }
    }

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
}