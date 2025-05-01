package com.achmadichzan.dicodingstory.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object ScrollPreferences {
    val INDEX = intPreferencesKey("scroll_index")
    val OFFSET = intPreferencesKey("scroll_offset")
}

val Context.scrollDataStore: DataStore<Preferences> by preferencesDataStore(name = "scroll_state")