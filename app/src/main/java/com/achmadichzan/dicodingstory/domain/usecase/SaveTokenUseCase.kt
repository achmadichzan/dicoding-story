package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.data.preferences.UserPreferencesImpl

class SaveTokenUseCase(private val preferences: UserPreferencesImpl) {
    suspend operator fun invoke(token: String) {
        preferences.saveToken(token)
    }
}