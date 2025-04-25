package com.achmadichzan.dicodingstory.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories ORDER BY createdAt DESC")
    fun getStories(): PagingSource<Int, StoryEntity>

    @Upsert
    suspend fun upsertAll(stories: List<StoryEntity>)

    @Query("DELETE FROM stories")
    suspend fun clearAll()
}
