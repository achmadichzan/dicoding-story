package com.achmadichzan.dicodingstory.domain.usecase

fun interface ClearTokenUseCase {
    suspend operator fun invoke()
}