package com.achmadichzan.dicodingstory.di

import androidx.room.Room
import com.achmadichzan.dicodingstory.data.local.preferences.UserPreferencesImpl
import com.achmadichzan.dicodingstory.data.local.room.StoryDatabase
import com.achmadichzan.dicodingstory.data.remote.service.ApiService
import com.achmadichzan.dicodingstory.data.remote.service.provideHttpClient
import com.achmadichzan.dicodingstory.data.repository.AuthRepositoryImpl
import com.achmadichzan.dicodingstory.data.repository.StoryRepositoryImpl
import com.achmadichzan.dicodingstory.domain.repository.AuthRepository
import com.achmadichzan.dicodingstory.domain.repository.StoryRepository
import com.achmadichzan.dicodingstory.domain.usecase.ClearTokenUseCase
import com.achmadichzan.dicodingstory.domain.usecase.GetDetailStoryUseCase
import com.achmadichzan.dicodingstory.domain.usecase.GetPagingStoryUseCase
import com.achmadichzan.dicodingstory.domain.usecase.GetTokenUseCase
import com.achmadichzan.dicodingstory.domain.usecase.LoginUseCase
import com.achmadichzan.dicodingstory.domain.usecase.RegisterUseCase
import com.achmadichzan.dicodingstory.domain.usecase.SaveTokenUseCase
import com.achmadichzan.dicodingstory.domain.usecase.UploadStoryUseCase
import com.achmadichzan.dicodingstory.presentation.viewmodel.DetailViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.LoginViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.RegisterViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.StoryViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.UploadViewModel
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
    single<StoryRepository> { StoryRepositoryImpl(get(), get(), get()) }
}

val useCaseModule = module {
    factory<LoginUseCase> {
        LoginUseCase { email, password ->
            get<AuthRepository>().login(email, password)
        }
    }
    factory<RegisterUseCase> {
        RegisterUseCase { name, email, password ->
            get<AuthRepository>().register(name, email, password)
        }
    }
    factory<ClearTokenUseCase> {
        ClearTokenUseCase {
            get<UserPreferencesImpl>().clearToken()
        }
    }
    factory<GetDetailStoryUseCase> {
        GetDetailStoryUseCase { id ->
            get<StoryRepository>().getStoryDetail(id)
        }
    }
    factory<GetTokenUseCase> {
        GetTokenUseCase {
            get<UserPreferencesImpl>().getToken()
        }
    }
    factory<SaveTokenUseCase> {
        SaveTokenUseCase { token ->
            get<UserPreferencesImpl>().saveToken(token)
        }
    }
    factory<UploadStoryUseCase> {
        UploadStoryUseCase { file, description, lat, lon ->
            get<StoryRepository>().uploadStory(file, description, lat, lon)
        }
    }
    factory<GetPagingStoryUseCase> {
        GetPagingStoryUseCase { token ->
            get<StoryRepository>().getPagedStories(token)
        }
    }
}


val viewModelModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { StoryViewModel(get(), get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { UploadViewModel(get()) }
}

val databaseModule = module {
    single<StoryDatabase> {
        Room.databaseBuilder(
            androidContext(),
            StoryDatabase::class.java,
            "story_database"
        ).build()
    }

    single { get<StoryDatabase>().storyDao() }
    single { get<StoryDatabase>().remoteKeysDao() }
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
    databaseModule,
    preferencesModule
)