package com.achmadichzan.dicodingstory.data.repository

import com.achmadichzan.dicodingstory.data.preferences.UserPreferencesImpl
import com.achmadichzan.dicodingstory.domain.model.StoryDto
import com.achmadichzan.dicodingstory.data.remote.service.ApiService
import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import com.achmadichzan.dicodingstory.domain.model.DetailResponse
import com.achmadichzan.dicodingstory.domain.repository.StoryRepository
import java.io.File

class StoryRepositoryImpl(
    private val apiService: ApiService,
    private val preferences: UserPreferencesImpl
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

    override suspend fun uploadStory(
        file: File,
        description: String,
        lat: Double?,
        lon: Double?
    ): BaseResponse {
        val token = preferences.getToken() ?: throw Exception("No token found")
        return apiService.uploadStory(token, file, description, lat, lon)
    }
}
