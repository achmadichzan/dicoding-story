package com.achmadichzan.dicodingstory.data.local.preferences

import android.content.Context
import com.achmadichzan.dicodingstory.data.local.preferences.UserPreferences

class UserPreferencesImpl(private val context: Context) {
    suspend fun saveToken(token: String) = UserPreferences.saveToken(context, token)
    suspend fun getToken(): String? = UserPreferences.getToken(context)
    suspend fun clearToken() = UserPreferences.clearToken(context)
}
