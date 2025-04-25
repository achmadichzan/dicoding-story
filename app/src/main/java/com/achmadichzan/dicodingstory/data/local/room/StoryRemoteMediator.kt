package com.achmadichzan.dicodingstory.data.local.room

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.achmadichzan.dicodingstory.data.local.mapper.toEntity
import com.achmadichzan.dicodingstory.domain.model.StoryResponse

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val storyDao: StoryDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val api: suspend (page: Int, size: Int) -> StoryResponse
): RemoteMediator<Int, StoryEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    val remoteKeys = lastItem?.let { remoteKeysDao.getRemoteKeyId(it.id) }
                    remoteKeys?.nextKey ?: return MediatorResult.Success(true)
                }
            }

            val response = api(page, state.config.pageSize)
            val stories = response.listStory.map { it.toEntity() }
            val endOfPagination = stories.isEmpty()

            if (loadType == LoadType.REFRESH) {
                remoteKeysDao.clearRemoteKeys()
                storyDao.clearAll()
            }

            val keys = stories.map {
                RemoteKeys(
                    id = it.id,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (endOfPagination) null else page + 1
                )
            }

            remoteKeysDao.insertAll(keys)
            storyDao.insertAll(stories)

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}