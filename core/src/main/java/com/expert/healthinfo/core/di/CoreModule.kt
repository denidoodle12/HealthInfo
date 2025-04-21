package com.expert.healthinfo.core.di

import androidx.room.Room
import com.expert.healthinfo.core.data.HealthRepository
import com.expert.healthinfo.core.data.source.local.LocalDataSource
import com.expert.healthinfo.core.data.source.local.room.HeadlinesDatabase
import com.expert.healthinfo.core.data.source.remote.RemoteDataSource
import com.expert.healthinfo.core.data.source.remote.network.ApiService
import com.expert.healthinfo.core.domain.repository.IheadlinesRepository
import com.expert.healthinfo.core.utils.AppExecutors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory {
        get<HeadlinesDatabase>().headlinesDao()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            HeadlinesDatabase::class.java, "Headlines.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repoModule = module {
    single {
        com.expert.healthinfo.core.data.source.local.LocalDataSource(get())
    }

    single {
        RemoteDataSource(get())
    }

    factory {
        AppExecutors()
    }

    single<IheadlinesRepository>{
        com.expert.healthinfo.core.data.HealthRepository(get(), get(), get())
    }
}