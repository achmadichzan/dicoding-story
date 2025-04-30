package com.achmadichzan.dicodingstory.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import com.achmadichzan.dicodingstory.domain.usecase.RegisterUseCase
import com.achmadichzan.dicodingstory.presentation.intent.RegisterIntent
import com.achmadichzan.dicodingstory.presentation.state.RegisterState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<RegisterIntent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    var state by mutableStateOf(RegisterState())
        private set

    fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.Submit -> register(intent.name, intent.email, intent.password)
            is RegisterIntent.NavigateBackToLogin -> {
                viewModelScope.launch {
                    _navigationEvent.emit(intent)
                }
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val result = registerUseCase(name, email, password)
                if (result.error) {
                    state = state.copy(isLoading = false, error = result.message)
                } else {
                    state = state.copy(isLoading = false, success = result)
                    _navigationEvent.emit(RegisterIntent.NavigateBackToLogin)
                }
            } catch (e: ClientRequestException) {
                val errorBody = e.response.bodyAsText()
                val errorMessage = try {
                    Json.decodeFromString(BaseResponse.serializer(), errorBody).message
                } catch (e: Exception) {
                    e.printStackTrace()
                    "Terjadi kesalahan saat memproses registrasi."
                }
                state = state.copy(isLoading = false, error = errorMessage)
            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    error = e.message ?: "Terjadi kesalahan tak terduga"
                )
            }
        }
    }
}