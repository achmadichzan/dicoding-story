package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.domain.model.BaseResponse

fun interface RegisterUseCase {
    suspend operator fun invoke(name: String, email: String, password: String): BaseResponse
}
