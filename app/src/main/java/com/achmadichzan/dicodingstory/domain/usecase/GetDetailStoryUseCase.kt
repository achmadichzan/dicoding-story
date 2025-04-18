package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.domain.model.DetailResponse
import com.achmadichzan.dicodingstory.domain.repository.StoryRepository

class GetDetailStoryUseCase(private val repository: StoryRepository) {
    suspend operator fun invoke(id: String): DetailResponse {
        return repository.getStoryDetail(id)
    }
}
