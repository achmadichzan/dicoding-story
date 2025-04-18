package com.achmadichzan.dicodingstory.presentation.util

sealed class RegisterIntent {
    data class Submit(
        val name: String,
        val email: String,
        val password: String
    ) : RegisterIntent()
}