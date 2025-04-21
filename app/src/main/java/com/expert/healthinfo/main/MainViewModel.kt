package com.expert.healthinfo.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.expert.healthinfo.core.domain.usecase.HeadlinesUseCase

class MainViewModel(headlinesUseCase: HeadlinesUseCase) : ViewModel() {

    val headlines = headlinesUseCase.getAllHeadlines().asLiveData()

}
