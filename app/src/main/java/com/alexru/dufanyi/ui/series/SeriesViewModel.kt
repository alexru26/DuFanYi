package com.alexru.dufanyi.ui.series

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexru.dufanyi.data.dao.ChaptersDao
import com.alexru.dufanyi.data.dao.PagesDao
import com.alexru.dufanyi.data.dao.SeriesDao
import com.alexru.dufanyi.data.entity.ChapterEntity
import com.alexru.dufanyi.data.entity.SeriesEntity
import com.alexru.dufanyi.ui.browse.BrowseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val seriesDao: SeriesDao,
    private val chaptersDao: ChaptersDao,
    private val pagesDao: PagesDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(SeriesUiState())

    private val seriesId = savedStateHandle.get<Long>("id")!!

    private val series = seriesDao.getSeries(seriesId)

    private val chaptersList = chaptersDao.getChapters(seriesId)

    val state: StateFlow<SeriesUiState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                series,
                chaptersList
            ) { series,
                chaptersList ->
                SeriesUiState(
                    series = series,
                    chaptersList = chaptersList,
                    errorMessage = null
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
    val series: SeriesEntity? = null,
    val chaptersList: List<ChapterEntity> = emptyList(),
    val errorMessage: String? = null
)