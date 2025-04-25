package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.domain.model.LoginResponse
import com.achmadichzan.dicodingstory.domain.model.LoginResult
import com.achmadichzan.dicodingstory.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): LoginResponse {
        return repository.login(email, password)
    }
}
