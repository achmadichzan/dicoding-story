package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.data.preferences.UserPreferencesImpl

class GetTokenUseCase(private val preferences: UserPreferencesImpl) {
    suspend operator fun invoke(): String? {
        return preferences.getToken()
    }
}