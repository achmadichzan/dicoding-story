package com.achmadichzan.dicodingstory.domain.preferences

import android.content.Context
import com.achmadichzan.dicodingstory.data.local.preferences.LocationPref
import kotlinx.coroutines.flow.Flow

class LocationPrefImpl(private val context: Context) {

    private val prefs = LocationPref(context)

    val isLocationEnabled: Flow<Boolean> = prefs.isLocationEnabled

    suspend fun setLocationEnabled(enabled: Boolean) {
        prefs.setLocationEnabled(enabled)
    }
}