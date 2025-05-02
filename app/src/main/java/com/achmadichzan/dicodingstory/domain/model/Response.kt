package com.achmadichzan.dicodingstory.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class BaseResponse(
    val error: Boolean,
    val message: String
): Parcelable

@Parcelize
@Serializable
data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult? = null
): Parcelable

@Parcelize
@Serializable
data class LoginResult(
    val userId: String,
    val name: String,
    val token: String
): Parcelable

@Parcelize
@Serializable
data class StoryResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<StoryDto>
): Parcelable

@Immutable
@Parcelize
@Serializable
data class StoryDto(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double? = null,
    val lon: Double? = null
): Parcelable

@Serializable
data class DetailResponse(
    val error: Boolean,
    val message: String,
    val story: StoryDetail
)

@Serializable
data class StoryDetail(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double? = null,
    val lon: Double? = null
)