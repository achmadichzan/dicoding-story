package com.achmadichzan.dicodingstory.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.dicodingstory.data.local.room.StoryEntity
import com.achmadichzan.dicodingstory.domain.usecase.GetLocationStoryUseCase
import com.achmadichzan.dicodingstory.presentation.intent.MapsLocationIntent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapsLocationViewModel(
    private val getStoriesUseCase: GetLocationStoryUseCase
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<MapsLocationIntent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _uiState = MutableStateFlow<List<StoryEntity>>(emptyList())
    val uiState: StateFlow<List<StoryEntity>> = _uiState.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onIntent(intent: MapsLocationIntent) {
        viewModelScope.launch {
            _navigationEvent.emit(intent)
        }
    }

    init {
        viewModelScope.launch {
            getStoriesUseCase()
                .catch { e -> _error.update { e.message } }
                .collect { stories ->
                    _uiState.update { stories }
                }
        }
    }
}