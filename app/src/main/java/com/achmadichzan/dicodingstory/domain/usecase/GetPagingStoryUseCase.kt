package com.achmadichzan.dicodingstory.domain.usecase

import androidx.paging.PagingData
import com.achmadichzan.dicodingstory.data.local.room.StoryEntity
import com.achmadichzan.dicodingstory.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class GetPagingStoryUseCase(
    private val repository: StoryRepository
) {
    operator fun invoke(token: String): Flow<PagingData<StoryEntity>> {
        return repository.getPagedStories(token)
    }
}