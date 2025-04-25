package com.achmadichzan.dicodingstory.data.local.mapper

import com.achmadichzan.dicodingstory.data.local.room.StoryEntity
import com.achmadichzan.dicodingstory.domain.model.StoryDto

fun StoryEntity.toDto(): StoryDto {
    return StoryDto(
        id = id,
        name = name,
        description = description,
        photoUrl = photoUrl,
        createdAt = createdAt,
        lat = lat,
        lon = lon
    )
}

