package com.achmadichzan.dicodingstory.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.dicodingstory.domain.usecase.GetDetailStoryUseCase
import com.achmadichzan.dicodingstory.presentation.state.DetailState
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getDetailStoryUseCase: GetDetailStoryUseCase
) : ViewModel() {

    var state by mutableStateOf<DetailState>(DetailState())
        private set

    fun loadDetail(id: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val result = getDetailStoryUseCase(id)
                state = state.copy(isLoading = false, story = result.story)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message)
            }
        }
    }
}