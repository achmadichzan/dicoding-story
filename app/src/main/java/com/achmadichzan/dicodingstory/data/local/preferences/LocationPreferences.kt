package com.achmadichzan.dicodingstory.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.locationDataStore by preferencesDataStore(name = "location_prefs")

class LocationPreferences(private val context: Context) {

    private val LOCATION_ENABLED_KEY = booleanPreferencesKey("is_location_enabled")

    val isLocationEnabled: Flow<Boolean> = context.locationDataStore.data
        .map { preferences -> preferences[LOCATION_ENABLED_KEY] == true }

    suspend fun setLocationEnabled(enabled: Boolean) {
        context.locationDataStore.edit { preferences ->
            preferences[LOCATION_ENABLED_KEY] = enabled
        }
    }
}