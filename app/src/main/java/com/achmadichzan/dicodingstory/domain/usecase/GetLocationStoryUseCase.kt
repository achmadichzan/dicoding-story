package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.data.local.room.StoryEntity
import kotlinx.coroutines.flow.Flow

fun interface GetLocationStoryUseCase {
    operator fun invoke(): Flow<List<StoryEntity>>
}
