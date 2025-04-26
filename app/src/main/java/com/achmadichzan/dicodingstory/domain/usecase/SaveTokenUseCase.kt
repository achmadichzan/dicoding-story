package com.achmadichzan.dicodingstory.domain.usecase

fun interface SaveTokenUseCase {
    suspend operator fun invoke(token: String)
}