package com.expert.healthinfo.di

import com.expert.healthinfo.core.domain.usecase.HeadlinesInteractor
import com.expert.healthinfo.core.domain.usecase.HeadlinesUseCase
import com.expert.healthinfo.detail.DetailViewModel
import com.expert.healthinfo.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<HeadlinesUseCase>{
        HeadlinesInteractor(get())
    }
}

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
    viewModel {
        DetailViewModel(get())
    }
}

