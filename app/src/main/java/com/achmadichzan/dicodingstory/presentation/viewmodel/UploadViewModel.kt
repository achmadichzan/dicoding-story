package com.achmadichzan.dicodingstory.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.dicodingstory.domain.usecase.UploadStoryUseCase
import com.achmadichzan.dicodingstory.presentation.state.UploadState
import com.achmadichzan.dicodingstory.presentation.intent.AddStoryIntent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File

class UploadViewModel(
    private val uploadStoryUseCase: UploadStoryUseCase
) : ViewModel() {

    var state by mutableStateOf(UploadState())
        private set

    private val _navigationEvent = MutableSharedFlow<AddStoryIntent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onIntent(intent: AddStoryIntent) {
        viewModelScope.launch {
            _navigationEvent.emit(intent)
        }
    }

    fun uploadStory(file: File, description: String, lat: Double?, lon: Double?) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val response = uploadStoryUseCase(file, description, lat, lon)
                state = state.copy(isLoading = false, success = response.message)
                onIntent(AddStoryIntent.GoBack)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message)
            }
        }
    }
}