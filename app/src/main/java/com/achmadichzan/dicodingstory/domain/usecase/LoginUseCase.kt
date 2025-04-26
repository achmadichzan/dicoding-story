package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.domain.model.LoginResponse

fun interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): LoginResponse
}