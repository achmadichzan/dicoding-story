package com.achmadichzan.dicodingstory.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories ORDER BY createdAt DESC")
    fun getStories(): PagingSource<Int, StoryEntity>

    @Upsert
    suspend fun upsertAll(stories: List<StoryEntity>)

    @Query("DELETE FROM stories")
    suspend fun clearAll()

    @Query("SELECT * FROM stories WHERE lat IS NOT NULL AND lon IS NOT NULL")
    fun observeStoriesWithLocation(): Flow<List<StoryEntity>>

    @Query("SELECT COUNT(*) FROM stories")
    suspend fun count(): Int
}