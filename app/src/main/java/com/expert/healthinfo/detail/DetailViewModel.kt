package com.expert.healthinfo.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.expert.healthinfo.core.domain.model.Headlines
import com.expert.healthinfo.core.domain.usecase.HeadlinesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val headlinesUseCase: HeadlinesUseCase) : ViewModel() {

    fun isHeadlineFavorite(idHeadlines: String): LiveData<Boolean> {
        return headlinesUseCase.isHeadlineFavorite(idHeadlines).asLiveData()
    }

    fun insertHeadlinesFavorite(headlines: Headlines) {
        viewModelScope.launch(Dispatchers.IO) {
            headlinesUseCase.insertFavoriteHeadlines(headlines)
        }
    }

    fun deleteHeadlinesFavorite(headlines: Headlines) {
        viewModelScope.launch(Dispatchers.IO) {
            headlinesUseCase.deleteFavoriteHeadlines(headlines)
        }
    }
}