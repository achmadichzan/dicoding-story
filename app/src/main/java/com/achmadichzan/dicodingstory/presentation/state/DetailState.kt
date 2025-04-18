package com.achmadichzan.dicodingstory.presentation.state

import com.achmadichzan.dicodingstory.domain.model.StoryDetail

data class DetailState(
    val isLoading: Boolean = false,
    val story: StoryDetail? = null,
    val error: String? = null
)
