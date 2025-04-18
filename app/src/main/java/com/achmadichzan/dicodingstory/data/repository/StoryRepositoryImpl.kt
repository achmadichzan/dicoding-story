package com.achmadichzan.dicodingstory.data.repository

import com.achmadichzan.dicodingstory.domain.model.StoryDto
import com.achmadichzan.dicodingstory.data.remote.service.ApiService
import com.achmadichzan.dicodingstory.domain.model.DetailResponse
import com.achmadichzan.dicodingstory.domain.repository.StoryRepository

class StoryRepositoryImpl(
    private val apiService: ApiService
) : StoryRepository {

    override suspend fun getAllStories(
        token: String?,
        page: Int?,
        size: Int?,
        location: Int?
    ): List<StoryDto> {
        val response = apiService.getStories(token, page, size, location)
        if (response.error) {
            throw Exception(response.message)
        }
        return response.listStory
    }

    override suspend fun getStoryDetail(id: String): DetailResponse {
        return apiService.getDetailStory(id)
    }
}
