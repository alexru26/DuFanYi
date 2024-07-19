package com.alexru.dufanyi.ui.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexru.dufanyi.data.dao.ChaptersDao
import com.alexru.dufanyi.data.dao.PagesDao
import com.alexru.dufanyi.data.dao.SeriesDao
import com.alexru.dufanyi.data.entity.ChapterEntity
import com.alexru.dufanyi.data.entity.PageEntity
import com.alexru.dufanyi.data.entity.SeriesEntity
import com.alexru.dufanyi.data.entity.SeriesWithChapters
import com.alexru.dufanyi.ui.series.SeriesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    seriesDao: SeriesDao,
    private val chaptersDao: ChaptersDao,
    pagesDao: PagesDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ReaderUiState())

    private val seriesId: Long = savedStateHandle.get<Long>("seriesId")!!

    private val series = seriesDao.getSeriesWithChapter(seriesId)

    val state: StateFlow<ReaderUiState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                series,
            ) { series ->
                ReaderUiState(
                    series = series[0],
                    errorMessage = null
                )
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

    fun onChapterRead(chapterId: Long, page: Int) {
        viewModelScope.launch {
            chaptersDao.updateChapterRead(chapterId, page)
        }

    }

    fun onChapterFinished(chapterId: Long) {
        viewModelScope.launch {
            chaptersDao.updateChapterRead(chapterId)
        }
    }

}

data class ReaderUiState(
    val series: SeriesWithChapters? = null,
    val errorMessage: String? = null
)