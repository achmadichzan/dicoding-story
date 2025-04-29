package com.achmadichzan.dicodingstory.di

import com.achmadichzan.dicodingstory.data.remote.service.ApiService
import com.achmadichzan.dicodingstory.data.remote.service.provideHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { provideHttpClient() }
    single { ApiService(get(), get()) }
}