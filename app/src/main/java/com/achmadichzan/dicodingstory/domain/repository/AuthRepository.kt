package com.achmadichzan.dicodingstory.domain.repository

import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import com.achmadichzan.dicodingstory.domain.model.LoginResponse

interface AuthRepository {

    suspend fun register(name: String, email: String, password: String): BaseResponse

    suspend fun login (email: String, password: String): LoginResponse
}