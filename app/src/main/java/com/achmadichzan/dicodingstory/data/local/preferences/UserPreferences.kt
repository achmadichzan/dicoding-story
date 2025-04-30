package com.achmadichzan.dicodingstory.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object UserPreferences {
    val Context.dataStore by preferencesDataStore(name = "user_prefs")
    private val TOKEN_KEY = stringPreferencesKey("token")

    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { prefs -> prefs[TOKEN_KEY] = token }
    }

    suspend fun getToken(context: Context): String? {
        return context.dataStore.data.map { it[TOKEN_KEY] }.first()
    }

    suspend fun clearToken(context: Context) {
        context.dataStore.edit { it.remove(TOKEN_KEY) }
    }
}