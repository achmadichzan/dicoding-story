package com.achmadichzan.dicodingstory.data.local.mapper

import com.achmadichzan.dicodingstory.data.local.room.StoryEntity
import com.achmadichzan.dicodingstory.domain.model.StoryDto

fun StoryDto.toEntity(): StoryEntity {
    return StoryEntity(
        id = id,
        name = name,
        description = description,
        photoUrl = photoUrl,
        createdAt = createdAt,
        lat = lat,
        lon = lon
    )
}
