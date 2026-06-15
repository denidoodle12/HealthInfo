package com.expert.healthinfo.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.expert.healthinfo.core.domain.model.Headlines
import com.expert.healthinfo.core.domain.usecase.HeadlinesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class MainViewModel(private val headlinesUseCase: HeadlinesUseCase) : ViewModel() {

    /** Kata kunci pencarian dari user input di SearchView */
    private val _searchQuery = MutableStateFlow("")

    /**
     * Trigger untuk memaksa refresh data.
     * Bertipe Int agar bisa increment walau nilai query tidak berubah
     * (StateFlow tidak emit nilai duplikat).
     */
    private val _refreshTrigger = MutableStateFlow(0)

    /**
     * Flow utama yang menggabungkan:
     * - Search query (dengan debounce 400ms untuk non-kosong, 0ms untuk kosong/clear)
     * - Refresh trigger (tanpa debounce — langsung re-fetch)
     *
     * flatMapLatest otomatis membatalkan API request sebelumnya saat query baru masuk.
     */
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val headlines = combine(
        _searchQuery.debounce { query -> if (query.isEmpty()) 0L else 400L },
        _refreshTrigger
    ) { query, _ ->
        query
    }
        .flatMapLatest { query ->
            headlinesUseCase.getAllHeadlines(query)
        }
        .asLiveData()

    /** Dipanggil dari SearchView.OnQueryTextListener di MainActivity */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query.trim()
    }

    /** Dipanggil dari tombol "Try Again" saat error */
    fun refresh() {
        _refreshTrigger.value += 1
    }

    /** Insert artikel ke daftar favorit */
    fun insertHeadlinesFavorite(headlines: Headlines) {
        viewModelScope.launch {
            headlinesUseCase.insertFavoriteHeadlines(headlines)
        }
    }

    /** Hapus artikel dari daftar favorit */
    fun deleteHeadlinesFavorite(headlines: Headlines) {
        viewModelScope.launch {
            headlinesUseCase.deleteFavoriteHeadlines(headlines)
        }
    }
}
