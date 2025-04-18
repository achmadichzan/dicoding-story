package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.domain.model.StoryDto
import com.achmadichzan.dicodingstory.domain.repository.StoryRepository

class GetStoriesUseCase(
    private val repository: StoryRepository
) {
    suspend operator fun invoke(
        token: String? = null,
        page: Int? = null,
        size: Int? = null,
        location: Int? = null
    ): List<StoryDto> {
        return repository.getAllStories(token, page, size, location)
    }
}
