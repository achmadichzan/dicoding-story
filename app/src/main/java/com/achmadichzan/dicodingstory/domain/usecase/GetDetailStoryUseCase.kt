package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.domain.model.DetailResponse

fun interface GetDetailStoryUseCase {
    suspend operator fun invoke(id: String): DetailResponse
}