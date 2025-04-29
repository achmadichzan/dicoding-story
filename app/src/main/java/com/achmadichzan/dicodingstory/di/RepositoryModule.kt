package com.achmadichzan.dicodingstory.di

import com.achmadichzan.dicodingstory.data.repository.AuthRepositoryImpl
import com.achmadichzan.dicodingstory.data.repository.StoryRepositoryImpl
import com.achmadichzan.dicodingstory.domain.repository.AuthRepository
import com.achmadichzan.dicodingstory.domain.repository.StoryRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<StoryRepository> { StoryRepositoryImpl(get(), get(), get()) }
}