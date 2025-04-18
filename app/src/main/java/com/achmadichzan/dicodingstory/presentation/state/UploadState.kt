package com.achmadichzan.dicodingstory.presentation.state

data class UploadState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null
)