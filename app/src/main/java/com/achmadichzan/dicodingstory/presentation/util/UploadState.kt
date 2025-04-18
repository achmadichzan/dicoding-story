package com.achmadichzan.dicodingstory.presentation.util

data class UploadState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null
)