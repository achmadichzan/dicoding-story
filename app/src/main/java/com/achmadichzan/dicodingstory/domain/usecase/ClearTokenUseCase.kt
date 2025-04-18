package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.data.preferences.UserPreferencesImpl

class ClearTokenUseCase(private val preferences: UserPreferencesImpl) {
    suspend operator fun invoke() {
        preferences.clearToken()
    }
}