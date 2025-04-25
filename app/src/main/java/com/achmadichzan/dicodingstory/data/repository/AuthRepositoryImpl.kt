package com.achmadichzan.dicodingstory.data.repository

import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import com.achmadichzan.dicodingstory.domain.model.LoginRequest
import com.achmadichzan.dicodingstory.domain.model.LoginResult
import com.achmadichzan.dicodingstory.domain.model.RegisterRequest
import com.achmadichzan.dicodingstory.data.remote.service.ApiService
import com.achmadichzan.dicodingstory.domain.model.LoginResponse
import com.achmadichzan.dicodingstory.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun register(name: String, email: String, password: String): BaseResponse {
        return apiService.register(RegisterRequest(name, email, password))
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(LoginRequest(email, password))
    }
}
