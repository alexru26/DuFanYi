package com.alexru.dufanyi.ui.browse

import android.net.Uri
import androidx.compose.ui.text.TextMeasurer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexru.dufanyi.data.repository.SeriesRepository
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
    private val seriesRepository: SeriesRepository,
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
            val (chaptersList, pagesList) = resourceManager.extractChaptersData(content, textMeasurer)
            seriesRepository.updateSeries(series, chaptersList, pagesList)
            isUploading.value = false
        }
    }

}

data class BrowseUiState(
    val isUploading: Boolean = false,
    val errorMessage: String? = null
)