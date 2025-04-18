package com.achmadichzan.dicodingstory.data.di

import com.achmadichzan.dicodingstory.data.preferences.UserPreferencesImpl
import com.achmadichzan.dicodingstory.data.remote.service.ApiService
import com.achmadichzan.dicodingstory.data.remote.service.provideHttpClient
import com.achmadichzan.dicodingstory.data.repository.AuthRepositoryImpl
import com.achmadichzan.dicodingstory.data.repository.StoryRepositoryImpl
import com.achmadichzan.dicodingstory.domain.repository.AuthRepository
import com.achmadichzan.dicodingstory.domain.repository.StoryRepository
import com.achmadichzan.dicodingstory.domain.usecase.ClearTokenUseCase
import com.achmadichzan.dicodingstory.domain.usecase.GetDetailStoryUseCase
import com.achmadichzan.dicodingstory.domain.usecase.GetStoriesUseCase
import com.achmadichzan.dicodingstory.domain.usecase.GetTokenUseCase
import com.achmadichzan.dicodingstory.domain.usecase.LoginUseCase
import com.achmadichzan.dicodingstory.domain.usecase.RegisterUseCase
import com.achmadichzan.dicodingstory.domain.usecase.SaveTokenUseCase
import com.achmadichzan.dicodingstory.presentation.viewmodel.DetailViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.LoginViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.RegisterViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.StoryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { provideHttpClient() }
    single { ApiService(get(), get()) }
}

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}

val storyModule = module {
    single<StoryRepository> { StoryRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { GetStoriesUseCase(get()) }
    single { GetDetailStoryUseCase(get()) }
    single { SaveTokenUseCase(get()) }
    single { GetTokenUseCase(get()) }
    single { ClearTokenUseCase(get()) }
}

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { StoryViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}

val preferencesModule = module {
    single { UserPreferencesImpl(androidContext()) }
}

val appModule = listOf(
    networkModule,
    authModule,
    storyModule,
    useCaseModule,
    viewModelModule,
    preferencesModule
)