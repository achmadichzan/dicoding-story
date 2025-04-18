package com.achmadichzan.dicodingstory.data.remote.service

import com.achmadichzan.dicodingstory.data.preferences.UserPreferencesImpl
import com.achmadichzan.dicodingstory.domain.model.BaseResponse
import com.achmadichzan.dicodingstory.domain.model.DetailResponse
import com.achmadichzan.dicodingstory.domain.model.LoginRequest
import com.achmadichzan.dicodingstory.domain.model.LoginResponse
import com.achmadichzan.dicodingstory.domain.model.RegisterRequest
import com.achmadichzan.dicodingstory.domain.model.StoryResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import java.io.File

class ApiService(
    private val client: HttpClient,
    private val preferences: UserPreferencesImpl
) {

    suspend fun register(request: RegisterRequest): BaseResponse {
        return client.post("v1/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun login(request: LoginRequest): LoginResponse {
        return client.post("v1/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<LoginResponse>()
    }

    suspend fun getStories(
        token: String? = null,
        page: Int? = null,
        size: Int? = null,
        location: Int? = null
    ): StoryResponse {
        val userToken = preferences.getToken() ?: token
        return client.get("v1/stories") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
            parameter("page", page)
            parameter("size", size)
            parameter("location", location)
        }.body()
    }

    suspend fun getDetailStory(id: String, token: String? = null): DetailResponse {
        val userToken = preferences.getToken() ?: token
        return client.get("v1/stories/$id") {
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }.body()
    }

    suspend fun uploadStory(
        token: String,
        file: File,
        description: String,
        lat: Double? = null,
        lon: Double? = null
    ): BaseResponse {
        val userToken = preferences.getToken() ?: token
        val response: BaseResponse = client.submitFormWithBinaryData(
            url = "v1/stories",
            formData = formData {
                append("description", description)
                append("photo", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, ContentType.Image.JPEG.contentType)
                    append(
                        HttpHeaders.ContentDisposition,
                        "form-data; name=\"photo\"; filename=\"${file.name}\""
                    )
                })
                lat?.let { append("lat", it.toString()) }
                lon?.let { append("lon", it.toString()) }
            }
        ) {
            header(HttpHeaders.Authorization, "Bearer $userToken")
        }.body()

        return response
    }

}