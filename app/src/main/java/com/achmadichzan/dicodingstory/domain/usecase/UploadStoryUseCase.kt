package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import java.io.File

fun interface UploadStoryUseCase {
    suspend operator fun invoke(
        file: File,
        description: String,
        lat: Double?,
        lon: Double?
    ): BaseResponse
}