package com.achmadichzan.dicodingstory.di

import com.achmadichzan.dicodingstory.data.local.preferences.UserPreferencesImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule = module {
    single { UserPreferencesImpl(androidContext()) }
}