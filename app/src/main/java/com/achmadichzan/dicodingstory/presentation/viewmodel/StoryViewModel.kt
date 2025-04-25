package com.achmadichzan.dicodingstory.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.achmadichzan.dicodingstory.data.local.mapper.toDto
import com.achmadichzan.dicodingstory.domain.model.StoryDto
import com.achmadichzan.dicodingstory.domain.usecase.GetPagingStoryUseCase
import com.achmadichzan.dicodingstory.domain.usecase.GetStoriesUseCase
import com.achmadichzan.dicodingstory.presentation.state.StoryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class StoryViewModel(
    private val getStoriesUseCase: GetStoriesUseCase,
    private val getPagingStoryUseCase: GetPagingStoryUseCase
) : ViewModel() {

    var state by mutableStateOf(StoryState())
        private set

    fun loadStories(token: String? = null, page: Int? = 1, size: Int? = 20, location: Int? = null) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val stories = getStoriesUseCase(token, page, size, location)
                state = state.copy(isLoading = false, stories = stories)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun getPagedStories(token: String): Flow<PagingData<StoryDto>> {
        return getPagingStoryUseCase(token)
            .map { pagingData ->
                pagingData.map { it.toDto() }
            }
            .cachedIn(viewModelScope)
    }
}
