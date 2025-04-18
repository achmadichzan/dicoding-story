package com.achmadichzan.dicodingstory.data.preferences

import android.content.Context
import com.achmadichzan.dicodingstory.data.local.UserPreferences

class UserPreferencesImpl(private val context: Context) {
    suspend fun saveToken(token: String) = UserPreferences.saveToken(context, token)
    suspend fun getToken(): String? = UserPreferences.getToken(context)
    suspend fun clearToken() = UserPreferences.clearToken(context)
}
