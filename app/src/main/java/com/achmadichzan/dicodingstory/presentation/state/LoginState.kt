package com.achmadichzan.dicodingstory.presentation.state

import com.achmadichzan.dicodingstory.domain.model.LoginResult

data class LoginState(
    val isLoading: Boolean = false,
    val success: LoginResult? = null,
    val error: String? = null
)