package com.achmadichzan.dicodingstory.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.achmadichzan.dicodingstory.data.local.mapper.toDto
import com.achmadichzan.dicodingstory.domain.model.StoryDto
import com.achmadichzan.dicodingstory.domain.usecase.GetPagingStoryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoryViewModel(
    private val getPagingStoryUseCase: GetPagingStoryUseCase
) : ViewModel() {

    fun getPagedStories(token: String): Flow<PagingData<StoryDto>> {
        return getPagingStoryUseCase(token)
            .map { pagingData ->
                pagingData.map { it.toDto() }
            }
            .cachedIn(viewModelScope)
    }
}
