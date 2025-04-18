package com.achmadichzan.dicodingstory.domain.usecase

import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import com.achmadichzan.dicodingstory.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, password: String): BaseResponse {
        return repository.register(name, email, password)
    }
}
