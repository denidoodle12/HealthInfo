package com.expert.healthinfo.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.expert.healthinfo.core.domain.usecase.HeadlinesUseCase

class FavoriteViewModel(headlinesUseCase: HeadlinesUseCase) : ViewModel() {
    val headlines = headlinesUseCase.getFavoriteHeadlines().asLiveData()
}