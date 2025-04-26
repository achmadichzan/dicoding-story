package com.achmadichzan.dicodingstory.domain.repository

import androidx.paging.PagingData
import com.achmadichzan.dicodingstory.data.local.room.StoryEntity
import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import com.achmadichzan.dicodingstory.domain.model.DetailResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoryRepository {

    suspend fun getStoryDetail(id: String): DetailResponse

    suspend fun uploadStory(
        file: File,
        description: String,
        lat: Double?,
        lon: Double?
    ): BaseResponse

    fun getPagedStories(token: String): Flow<PagingData<StoryEntity>>
}
