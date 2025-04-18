package com.achmadichzan.dicodingstory.presentation.state

import com.achmadichzan.dicodingstory.domain.model.StoryDto

data class StoryState(
    val isLoading: Boolean = false,
    val stories: List<StoryDto> = emptyList(),
    val error: String? = null
)