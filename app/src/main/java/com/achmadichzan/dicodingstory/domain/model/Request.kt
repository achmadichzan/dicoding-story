package com.achmadichzan.dicodingstory.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
): Parcelable

@Parcelize
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
): Parcelable
