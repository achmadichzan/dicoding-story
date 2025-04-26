package com.achmadichzan.dicodingstory.domain.usecase

import androidx.paging.PagingData
import com.achmadichzan.dicodingstory.data.local.room.StoryEntity
import kotlinx.coroutines.flow.Flow

fun interface GetPagingStoryUseCase {
    operator fun invoke(token: String): Flow<PagingData<StoryEntity>>
}
