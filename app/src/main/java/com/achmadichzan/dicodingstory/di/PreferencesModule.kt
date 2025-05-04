package com.achmadichzan.dicodingstory.di

import com.achmadichzan.dicodingstory.domain.preferences.LocationPrefImpl
import com.achmadichzan.dicodingstory.domain.preferences.SessionManagerPrefImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule = module {
    single { SessionManagerPrefImpl(androidContext()) }
    single { LocationPrefImpl(androidContext()) }
}