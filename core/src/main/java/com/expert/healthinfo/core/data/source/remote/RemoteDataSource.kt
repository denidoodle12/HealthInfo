package com.expert.healthinfo.core.data.source.remote

import android.util.Log
import com.expert.healthinfo.core.data.source.remote.network.ApiResponse
import com.expert.healthinfo.core.data.source.remote.network.ApiService
import com.expert.healthinfo.core.data.source.remote.response.HeadlinesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getAllTourism(): Flow<ApiResponse<List<HeadlinesResponse>>> {
        return flow {
            try {
                val response = apiService.getListHeadlines()
                val data = response.articles
                if (data.isNotEmpty()) {
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

}