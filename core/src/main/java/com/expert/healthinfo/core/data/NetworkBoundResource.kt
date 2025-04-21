package com.expert.healthinfo.core.data

import com.expert.healthinfo.core.data.source.remote.network.ApiResponse
import com.expert.healthinfo.core.utils.AppExecutors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

abstract class NetworkBoundResource<ResultType, RequestType>(private val mExecutors: AppExecutors) {

    private val result: Flow<com.expert.healthinfo.core.data.Result<ResultType>> = flow {
        emit(com.expert.healthinfo.core.data.Result.Loading())
        when (val apiResponse = createCall().first()) {
            is ApiResponse.Success -> {
                emitAll(loadFromNetwork(apiResponse.data).map {
                    com.expert.healthinfo.core.data.Result.Success(it)
                })
            }

            is ApiResponse.Error -> {
                emit(com.expert.healthinfo.core.data.Result.Error<ResultType>(apiResponse.errorMessage))
            }

            else -> {}
        }
    }

    protected abstract fun loadFromNetwork(data: RequestType): Flow<ResultType>

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    fun asFlow(): Flow<com.expert.healthinfo.core.data.Result<ResultType>> = result
}