package com.alexru.dufanyi.ui.series

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexru.dufanyi.data.dao.ChaptersDao
import com.alexru.dufanyi.data.dao.PagesDao
import com.alexru.dufanyi.data.dao.SeriesDao
import com.alexru.dufanyi.data.entity.ChapterEntity
import com.alexru.dufanyi.data.entity.SeriesEntity
import com.alexru.dufanyi.data.store.SeriesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel @Inject constructor(
    seriesStore: SeriesStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(SeriesUiState())

    private val seriesId = savedStateHandle.get<Long>("id")!!

    private val series = seriesStore.getSeriesWithChapter(seriesId)

    val state: StateFlow<SeriesUiState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                series
            ) { series ->
                SeriesUiState(
                    name = series[0].series.name,
                    author = series[0].series.author,
                    status = series[0].series.status,
                    chaptersList = series[0].chapters
                )
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

    fun onDeleteSeries() {
//        viewModelScope.launch {
//            seriesDao.deleteSeries(seriesId)
//            chaptersDao.deleteChapters(seriesId)
//            pagesDao.deletePages(seriesId)
//        }
    }

}

data class SeriesUiState(
    val name: String = "",
    val author: String = "",
    val status: String = "",
    val chaptersList: List<ChapterEntity> = emptyList(),
)