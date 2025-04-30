package com.achmadichzan.dicodingstory.di

import com.achmadichzan.dicodingstory.domain.preferences.LocationPreferencesImpl
import com.achmadichzan.dicodingstory.domain.preferences.SessionManagerPreferencesImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule = module {
    single { SessionManagerPreferencesImpl(androidContext()) }
    single { LocationPreferencesImpl(androidContext()) }
}