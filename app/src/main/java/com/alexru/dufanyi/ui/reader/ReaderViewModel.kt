package com.alexru.dufanyi.ui.reader

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

    var state by mutableStateOf(ReaderUiState())
        private set

    init {
        viewModelScope.launch {
            val seriesId: Long = savedStateHandle.get<Long>("seriesId")!!
            val chapterId: Long = savedStateHandle.get<Long>("chapterId")!!

            val series = seriesStore.getSeriesWithChapter(seriesId).first()

            state = ReaderUiState(
                seriesId = seriesId,
                chapterId = chapterId,
                chaptersList = series.chapters,
                pagesList = series.pages
            )
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

}

data class ReaderUiState(
    val seriesId: Long = 0,
    val chapterId: Long = 0,
    val chaptersList: List<ChapterEntity> = emptyList(),
    val pagesList: List<PageEntity> = emptyList(),
    val errorMessage: String? = null
)