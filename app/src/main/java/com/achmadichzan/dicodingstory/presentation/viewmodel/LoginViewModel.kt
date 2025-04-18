package com.achmadichzan.dicodingstory.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import com.achmadichzan.dicodingstory.domain.usecase.LoginUseCase
import com.achmadichzan.dicodingstory.domain.usecase.SaveTokenUseCase
import com.achmadichzan.dicodingstory.presentation.util.LoginIntent
import com.achmadichzan.dicodingstory.presentation.util.LoginState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Submit -> login(intent.email, intent.password)
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val result = loginUseCase(email, password)
                saveTokenUseCase(result?.token.orEmpty())
                state = state.copy(isLoading = false, success = result)
            } catch (e: ClientRequestException) {
                val errorBody = e.response.bodyAsText()
                val errorMessage = try {
                    Json.decodeFromString(BaseResponse.serializer(), errorBody).message
                } catch (e: Exception) {
                    e.printStackTrace()
                    "Unexpected error"
                }
                Log.d(LoginViewModel::class.java.simpleName, "errorBody : $errorBody, errorMessage: $errorMessage")
                state = state.copy(isLoading = false, error = errorMessage)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message ?: "Unknown error")
            }
        }
    }

}