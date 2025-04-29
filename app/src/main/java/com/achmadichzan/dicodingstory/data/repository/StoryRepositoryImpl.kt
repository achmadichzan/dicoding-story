package com.achmadichzan.dicodingstory.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.achmadichzan.dicodingstory.data.local.preferences.UserPreferencesImpl
import com.achmadichzan.dicodingstory.data.local.room.StoryDatabase
import com.achmadichzan.dicodingstory.data.local.room.StoryEntity
import com.achmadichzan.dicodingstory.data.remote.paging.StoryRemoteMediator
import com.achmadichzan.dicodingstory.data.remote.service.ApiService
import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import com.achmadichzan.dicodingstory.domain.model.DetailResponse
import com.achmadichzan.dicodingstory.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

class StoryRepositoryImpl(
    private val apiService: ApiService,
    private val preferences: UserPreferencesImpl,
    private val database: StoryDatabase
) : StoryRepository {

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

    override fun getPagedStories(token: String): Flow<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 1,
                initialLoadSize = 10,
                enablePlaceholders = false
            ),
            remoteMediator = StoryRemoteMediator(
                database = database,
                apiService = apiService
            ),
            pagingSourceFactory = {
                database.storyDao().getStories()
            }
        ).flow
    }

    override fun observeStoriesWithLocation(): Flow<List<StoryEntity>> {
        return database.storyDao().observeStoriesWithLocation()
    }
}