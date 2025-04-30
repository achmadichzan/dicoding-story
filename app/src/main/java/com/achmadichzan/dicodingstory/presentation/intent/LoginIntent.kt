package com.achmadichzan.dicodingstory.presentation.intent

sealed class LoginIntent {
    data class Submit(
        val email: String,
        val password: String
    ) : LoginIntent()
    data object NavigateToRegister : LoginIntent()
    data object NavigateToStory : LoginIntent()
}