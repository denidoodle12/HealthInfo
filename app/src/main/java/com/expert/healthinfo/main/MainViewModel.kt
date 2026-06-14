package com.expert.healthinfo.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.expert.healthinfo.core.domain.usecase.HeadlinesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class MainViewModel(private val headlinesUseCase: HeadlinesUseCase) : ViewModel() {

    // Trigger untuk refresh data. Setiap kali nilainya berubah, flatMapLatest
    // akan membatalkan flow sebelumnya dan mulai yang baru (re-fetch dari API).
    private val _refreshTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val headlines = _refreshTrigger
        .flatMapLatest { headlinesUseCase.getAllHeadlines() }
        .asLiveData()

    /** Dipanggil dari UI saat user tap tombol "Try Again" */
    fun refresh() {
        _refreshTrigger.value += 1
    }
}
