package com.achmadichzan.dicodingstory.presentation.util

sealed class LoginIntent {
    data class Submit(
        val email: String,
        val password: String
    ) : LoginIntent()
}