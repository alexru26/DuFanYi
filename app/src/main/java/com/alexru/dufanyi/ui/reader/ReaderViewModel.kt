package com.alexru.dufanyi.ui.reader

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexru.dufanyi.data.dao.ChaptersDao
import com.alexru.dufanyi.data.dao.PagesDao
import com.alexru.dufanyi.data.dao.SeriesDao
import com.alexru.dufanyi.data.entity.ChapterEntity
import com.alexru.dufanyi.data.entity.PageEntity
import com.alexru.dufanyi.data.entity.SeriesEntity
import com.alexru.dufanyi.data.entity.SeriesWithChapters
import com.alexru.dufanyi.data.store.ChaptersStore
import com.alexru.dufanyi.data.store.SeriesStore
import com.alexru.dufanyi.ui.series.SeriesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    seriesStore: SeriesStore,
    private val chaptersStore: ChaptersStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ReaderUiState())

    private val seriesId: Long = savedStateHandle.get<Long>("seriesId")!!
    private val chapterId: Long = savedStateHandle.get<Long>("chapterId")!!

    private val chapterName = MutableStateFlow("")

    private var showBars = MutableStateFlow(false)

    val state: StateFlow<ReaderUiState>
        get() = _state

    init {
        viewModelScope.launch {
            val series = seriesStore.getSeriesWithChapter(seriesId).first()
            val chapter = chaptersStore.getChapter(chapterId).first()
            chapterName.value = chapter.name

            combine(
                chapterName,
                showBars
            ) { chapterName,
                showBars ->
                ReaderUiState(
                    series = series.series.name,
                    chapterName = chapterName,
                    seriesId = seriesId,
                    chapterId = chapterId,
                    chaptersList = series.chapters,
                    pagesList = series.pages,
                    showBars = showBars,
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
            chaptersStore.updateChapterRead(chapterId, page)
        }

    }

    fun onChapterFinished(chapterId: Long) {
        viewModelScope.launch {
            chaptersStore.updateChapterRead(chapterId)
        }
    }

    fun showBars(value: Boolean) {
        showBars.value = value
    }

    fun updateChapterName(name: String) {
        if(chapterName.value != name) {
            chapterName.value = name
        }
    }
}

data class ReaderUiState(
    val series: String = "",
    val chapterName: String = "",
    val seriesId: Long = 0,
    val chapterId: Long = 0,
    val chaptersList: List<ChapterEntity> = emptyList(),
    val pagesList: List<PageEntity> = emptyList(),
    val showBars: Boolean = false,
    val errorMessage: String? = null
)