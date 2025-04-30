package com.achmadichzan.dicodingstory.presentation.intent

sealed class AddStoryIntent {
    data object GoBack : AddStoryIntent()
    data class ToggleLocation(val enabled: Boolean) : AddStoryIntent()
}