package com.achmadichzan.dicodingstory.domain.usecase

fun interface GetTokenUseCase {
    suspend operator fun invoke(): String?
}