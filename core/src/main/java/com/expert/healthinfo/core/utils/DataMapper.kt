package com.expert.healthinfo.core.utils

import com.expert.healthinfo.core.data.source.local.entity.HeadlinesEntity
import com.expert.healthinfo.core.data.source.remote.response.HeadlinesResponse
import com.expert.healthinfo.core.domain.model.Headlines
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

object DataMapper {

    /**
     * Maps a list of [HeadlinesResponse] (from API) to a [Flow] of domain [Headlines] list.
     */
    fun mapResponsesToDomain(input: List<HeadlinesResponse>): Flow<List<Headlines>> {
        return flowOf(
            input.map { response ->
                Headlines(
                    idHeadlines = response.url ?: UUID.randomUUID().toString(),
                    author = response.author,
                    urlToImage = response.urlToImage,
                    description = response.description,
                    title = response.title,
                    isFavorite = false
                )
            }
        )
    }

    /**
     * Maps a list of [HeadlinesEntity] (from Room) to a list of domain [Headlines].
     */
    fun mapEntitiesToDomain(input: List<HeadlinesEntity>): List<Headlines> {
        return input.map { entity ->
            Headlines(
                idHeadlines = entity.idHeadlines,
                author = entity.author,
                urlToImage = entity.urlToImage,
                description = entity.description,
                title = entity.title,
                isFavorite = entity.isFavorite
            )
        }
    }

    /**
     * Maps a single domain [Headlines] to a [HeadlinesEntity] for persistence.
     */
    fun mapDomainToEntity(input: Headlines) = HeadlinesEntity(
        idHeadlines = input.idHeadlines ?: UUID.randomUUID().toString(),
        author = input.author,
        urlToImage = input.urlToImage,
        description = input.description,
        title = input.title,
        isFavorite = input.isFavorite
    )
}