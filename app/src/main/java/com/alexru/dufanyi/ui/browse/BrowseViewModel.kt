package com.alexru.dufanyi.ui.browse

import android.net.Uri
import androidx.compose.ui.text.TextMeasurer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.alexru.dufanyi.data.dao.ChaptersDao
import com.alexru.dufanyi.data.dao.PagesDao
import com.alexru.dufanyi.data.dao.SeriesDao
import com.alexru.dufanyi.util.ResourceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val seriesDao: SeriesDao,
    private val chaptersDao: ChaptersDao,
    private val pagesDao: PagesDao,
    private val resourceManager: ResourceManager,
) : ViewModel() {

    private val _state = MutableStateFlow(BrowseUiState())

    private val isUploading = MutableStateFlow(false)

    val state: StateFlow<BrowseUiState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                isUploading
            ) { isUploading, ->
                BrowseUiState(
                    isUploading = isUploading[0],
                    errorMessage = null
                )
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

    fun onUpload(uri: Uri, textMeasurer: TextMeasurer) {
        isUploading.value = true
        viewModelScope.launch {
            val content = resourceManager.readTextFromUri(uri = uri)
            val series = resourceManager.extractSeriesData(content = content)
            seriesDao.insert(series)
            val seriesId = seriesDao.getSeriesByName(series.name).seriesId
            val (chaptersList, pagesList) = resourceManager.extractChaptersData(content, seriesId, textMeasurer)
            chaptersDao.insertAll(chaptersList)
            pagesDao.insertAll(pagesList)
            isUploading.value = false
        }
    }

}

data class BrowseUiState(
    val isUploading: Boolean = false,
    val errorMessage: String? = null
)