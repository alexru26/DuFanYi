package com.alexru.dufanyi.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexru.dufanyi.data.dao.SeriesDao
import com.alexru.dufanyi.data.entity.SeriesEntity
import com.alexru.dufanyi.data.entity.SeriesWithChapters
import com.alexru.dufanyi.data.store.SeriesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    seriesStore: SeriesStore
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryUiState())

    private val seriesList = seriesStore.getAllSeries()

    val state: StateFlow<LibraryUiState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                seriesList
            ) { booksList, ->
                LibraryUiState(
                    seriesList = booksList[0],
                    errorMessage = null
                )
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

}

data class LibraryUiState(
    val seriesList: List<SeriesEntity> = emptyList(),
    val errorMessage: String? = null
)