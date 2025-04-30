package com.achmadichzan.dicodingstory.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.achmadichzan.dicodingstory.data.local.mapper.toDto
import com.achmadichzan.dicodingstory.domain.model.StoryDto
import com.achmadichzan.dicodingstory.domain.usecase.ClearTokenUseCase
import com.achmadichzan.dicodingstory.domain.usecase.GetPagingStoryUseCase
import com.achmadichzan.dicodingstory.presentation.util.SessionManager
import com.achmadichzan.dicodingstory.presentation.intent.StoryIntent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoryViewModel(
    private val getPagingStoryUseCase: GetPagingStoryUseCase,
    private val clearTokenUseCase: ClearTokenUseCase
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<StoryIntent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _token = MutableStateFlow("")
    fun setToken(token: String) {
        _token.update { token }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedStories: Flow<PagingData<StoryDto>> = _token
        .flatMapLatest { token ->
            getPagingStoryUseCase(token)
                .map { pagingData -> pagingData.map { it.toDto() } }
        }
        .cachedIn(viewModelScope)

    fun onIntent(intent: StoryIntent) {
        when (intent) {
            is StoryIntent.Logout -> {
                viewModelScope.launch {
                    clearTokenUseCase()
                    SessionManager.token = null
                    _navigationEvent.emit(StoryIntent.Logout)
                }
            }
            is StoryIntent.OpenDetail -> {
                viewModelScope.launch {
                    _navigationEvent.emit(StoryIntent.OpenDetail(intent.storyId))
                }
            }
            is StoryIntent.AddStory -> {
                viewModelScope.launch {
                    _navigationEvent.emit(StoryIntent.AddStory)
                }
            }
            is StoryIntent.MapsLocation -> {
                viewModelScope.launch {
                    _navigationEvent.emit(StoryIntent.MapsLocation)
                }
            }
        }
    }
}