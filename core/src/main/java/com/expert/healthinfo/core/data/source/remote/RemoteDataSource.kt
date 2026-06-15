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

    /**
     * @param query Kata kunci pencarian. Jika kosong/blank, mengembalikan
     *              top health headlines tanpa filter (parameter q tidak dikirim ke API).
     */
    suspend fun getAllHeadlines(query: String = ""): Flow<ApiResponse<List<HeadlinesResponse>>> {
        return flow {
            try {
                val response = apiService.getListHeadlines(
                    q = if (query.isBlank()) null else query
                )
                // Selalu emit Success (termasuk list kosong) agar NetworkBoundResource
                // bisa menghasilkan Result.Success(emptyList()) untuk empty search results.
                emit(ApiResponse.Success(response.articles))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}