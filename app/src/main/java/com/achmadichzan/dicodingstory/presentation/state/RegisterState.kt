package com.achmadichzan.dicodingstory.presentation.state

import com.achmadichzan.dicodingstory.domain.model.BaseResponse

data class RegisterState(
    val isLoading: Boolean = false,
    val success: BaseResponse? = null,
    val error: String? = null
)