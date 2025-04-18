package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import com.achmadichzan.dicodingstory.domain.repository.StoryRepository
import java.io.File

class UploadStoryUseCase(private val repository: StoryRepository) {
    suspend operator fun invoke(
        file: File,
        description: String,
        lat: Double? = null,
        lon: Double? = null
    ): BaseResponse {
        return repository.uploadStory(file, description, lat, lon)
    }
}
