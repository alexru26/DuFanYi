package com.alexru.dufanyi.ui.library

import androidx.lifecycle.ViewModel
import com.alexru.dufanyi.database.entity.SeriesWithChapters
import com.alexru.dufanyi.database.getSeriesDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LibraryViewModel : ViewModel() {
    private val _state = MutableStateFlow(LibraryUiState())
    val state: StateFlow<LibraryUiState>
        get() = _state
}

data class LibraryUiState(
    val library: List<SeriesWithChapters> = listOf()
)