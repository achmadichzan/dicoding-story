package com.achmadichzan.dicodingstory.presentation.util

sealed class StoryIntent {
    data object Logout : StoryIntent()
    data class OpenDetail(val storyId: String) : StoryIntent()
    data object AddStory : StoryIntent()
    data object MapsLocation : StoryIntent()
}
