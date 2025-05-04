package com.achmadichzan.dicodingstory.domain.preferences

import android.content.Context
import com.achmadichzan.dicodingstory.data.local.preferences.SessionManagerPref

class SessionManagerPrefImpl(private val context: Context) {
    suspend fun saveToken(token: String) = SessionManagerPref.saveToken(context, token)
    suspend fun getToken(): String? = SessionManagerPref.getToken(context)
    suspend fun clearToken() = SessionManagerPref.clearToken(context)
}