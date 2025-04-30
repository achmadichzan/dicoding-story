package com.achmadichzan.dicodingstory.presentation.intent

sealed class DetailIntent {
    data class LoadDetail(val id: String) : DetailIntent()
    data object GoBack : DetailIntent()
}