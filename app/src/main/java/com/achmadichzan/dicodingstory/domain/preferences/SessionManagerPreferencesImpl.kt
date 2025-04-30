package com.achmadichzan.dicodingstory.domain.preferences

import android.content.Context
import com.achmadichzan.dicodingstory.data.local.preferences.SessionManagerPreferences

class SessionManagerPreferencesImpl(private val context: Context) {
    suspend fun saveToken(token: String) = SessionManagerPreferences.saveToken(context, token)
    suspend fun getToken(): String? = SessionManagerPreferences.getToken(context)
    suspend fun clearToken() = SessionManagerPreferences.clearToken(context)
}