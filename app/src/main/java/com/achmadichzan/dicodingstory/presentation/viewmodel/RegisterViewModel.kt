package com.achmadichzan.dicodingstory.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.dicodingstory.domain.usecase.RegisterUseCase
import com.achmadichzan.dicodingstory.presentation.util.RegisterIntent
import com.achmadichzan.dicodingstory.presentation.util.RegisterState
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.Submit -> register(intent.name, intent.email, intent.password)
        }
    }

    private fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val result = registerUseCase(name, email, password)
                state = state.copy(isLoading = false, success = result)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message)
            }
        }
    }
}
