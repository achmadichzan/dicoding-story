package com.achmadichzan.dicodingstory.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import com.achmadichzan.dicodingstory.domain.usecase.LoginUseCase
import com.achmadichzan.dicodingstory.domain.usecase.SaveTokenUseCase
import com.achmadichzan.dicodingstory.presentation.state.LoginState
import com.achmadichzan.dicodingstory.presentation.intent.LoginIntent
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<LoginIntent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    var state by mutableStateOf(LoginState())
        private set

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Submit -> login(intent.email, intent.password)
            is LoginIntent.NavigateToRegister,
            is LoginIntent.NavigateToStory -> {
                viewModelScope.launch {
                    _navigationEvent.emit(intent)
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val result = loginUseCase(email, password)
                if (result.error) {
                    state = state.copy(isLoading = false, error = result.message)
                } else {
                    saveTokenUseCase(result.loginResult?.token.orEmpty())
                    state = state.copy(isLoading = false, success = result.loginResult)
                    _navigationEvent.emit(LoginIntent.NavigateToStory)
                }
            } catch (e: ClientRequestException) {
                val errorBody = e.response.bodyAsText()
                val errorMessage = try {
                    Json.decodeFromString(BaseResponse.serializer(), errorBody).message
                } catch (e: Exception) {
                    e.printStackTrace()
                    "Terjadi kesalahan saat memproses login."
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