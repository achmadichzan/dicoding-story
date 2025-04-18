package com.achmadichzan.dicodingstory.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.dicodingstory.domain.usecase.GetStoriesUseCase
import com.achmadichzan.dicodingstory.presentation.util.StoryState
import kotlinx.coroutines.launch

class StoryViewModel(
    private val getStoriesUseCase: GetStoriesUseCase
) : ViewModel() {

    var state by mutableStateOf(StoryState())
        private set

    fun loadStories(token: String? = null, page: Int? = null, size: Int? = null, location: Int? = null) {
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
}
