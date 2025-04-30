package com.achmadichzan.dicodingstory.domain.preferences

import android.content.Context
import com.achmadichzan.dicodingstory.data.local.preferences.LocationPreferences
import kotlinx.coroutines.flow.Flow

class LocationPreferencesImpl(private val context: Context) {

    private val prefs = LocationPreferences(context)

    val isLocationEnabled: Flow<Boolean> = prefs.isLocationEnabled

    suspend fun setLocationEnabled(enabled: Boolean) {
        prefs.setLocationEnabled(enabled)
    }
}