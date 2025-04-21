package com.expert.healthinfo.core.data

sealed class Result<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : com.expert.healthinfo.core.data.Result<T>(data)
    class Loading<T>(data: T? = null) : com.expert.healthinfo.core.data.Result<T>(data)
    class Error<T>(message: String, data: T? = null) : com.expert.healthinfo.core.data.Result<T>(data, message)
}