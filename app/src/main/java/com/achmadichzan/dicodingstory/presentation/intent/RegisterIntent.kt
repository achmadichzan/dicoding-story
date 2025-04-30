package com.achmadichzan.dicodingstory.presentation.intent

sealed class RegisterIntent {
    data class Submit(
        val name: String,
        val email: String,
        val password: String
    ) : RegisterIntent()
    data object NavigateBackToLogin : RegisterIntent()
}