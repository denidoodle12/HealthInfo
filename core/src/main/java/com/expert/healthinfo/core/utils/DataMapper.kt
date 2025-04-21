package com.expert.healthinfo.core.utils

import com.expert.healthinfo.core.data.source.local.entity.HeadlinesEntity
import com.expert.healthinfo.core.data.source.remote.response.HeadlinesResponse
import com.expert.healthinfo.core.domain.model.Headlines
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

object DataMapper {
    fun mapResponsesToDomain(input: List<HeadlinesResponse>): Flow<List<Headlines>> {
        val data = ArrayList<Headlines>()
        input.map {
            val headlines = Headlines(
                it.source?.id,
                it.author,
                it.urlToImage,
                it.description,
                it.title,
                false
            )
            data.add(headlines)
        }
        return flowOf(data)
    }

    fun mapResponsesToEntities(input: List<HeadlinesResponse>): List<HeadlinesEntity> {
        val headlinesList = ArrayList<HeadlinesEntity>()
        input.map {
            val headlines = HeadlinesEntity(
                idHeadlines = it.source?.id ?: UUID.randomUUID().toString(),
                author = it.author,
                urlToImage = it.urlToImage,
                description = it.description,
                title = it.title,
                isFavorite = false
            )
            headlinesList.add(headlines)
        }
        return headlinesList
    }

    fun mapEntitiesToDomain(input: List<HeadlinesEntity>): List<Headlines> {
        val headlinesList = ArrayList<Headlines>()
        input.map {
            val headlines = Headlines(
                it.idHeadlines,
                it.author,
                it.urlToImage,
                it.description,
                it.title,
                it.isFavorite
            )
            headlinesList.add(headlines)
        }
        return headlinesList
    }

    fun mapDomainToEntity(input: Headlines) = HeadlinesEntity(
        input.idHeadlines ?: UUID.randomUUID().toString(),
        input.author,
        input.urlToImage,
        input.description,
        input.title,
        input.isFavorite
    )
}