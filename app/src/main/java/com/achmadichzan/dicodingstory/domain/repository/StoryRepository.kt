package com.achmadichzan.dicodingstory.domain.repository

import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import com.achmadichzan.dicodingstory.domain.model.DetailResponse
import com.achmadichzan.dicodingstory.domain.model.StoryDto
import java.io.File

interface StoryRepository {
    suspend fun getAllStories(
        token: String? = null,
        page: Int? = null,
        size: Int? = null,
        location: Int? = null
    ): List<StoryDto>

    suspend fun getStoryDetail(id: String): DetailResponse

    suspend fun uploadStory(
        file: File,
        description: String,
        lat: Double?,
        lon: Double?
    ): BaseResponse
}
