package com.achmadichzan.dicodingstory.di

import com.achmadichzan.dicodingstory.domain.preferences.SessionManagerPrefImpl
import com.achmadichzan.dicodingstory.domain.repository.AuthRepository
import com.achmadichzan.dicodingstory.domain.repository.StoryRepository
import com.achmadichzan.dicodingstory.domain.usecase.ClearTokenUseCase
import com.achmadichzan.dicodingstory.domain.usecase.GetDetailStoryUseCase
import com.achmadichzan.dicodingstory.domain.usecase.GetLocationStoryUseCase
import com.achmadichzan.dicodingstory.domain.usecase.GetPagingStoryUseCase
import com.achmadichzan.dicodingstory.domain.usecase.GetTokenUseCase
import com.achmadichzan.dicodingstory.domain.usecase.LoginUseCase
import com.achmadichzan.dicodingstory.domain.usecase.RegisterUseCase
import com.achmadichzan.dicodingstory.domain.usecase.SaveTokenUseCase
import com.achmadichzan.dicodingstory.domain.usecase.UploadStoryUseCase
import org.koin.dsl.module

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
            get<SessionManagerPrefImpl>().clearToken()
        }
    }
    factory<GetDetailStoryUseCase> {
        GetDetailStoryUseCase { id ->
            get<StoryRepository>().getStoryDetail(id)
        }
    }
    factory<GetTokenUseCase> {
        GetTokenUseCase {
            get<SessionManagerPrefImpl>().getToken()
        }
    }
    factory<SaveTokenUseCase> {
        SaveTokenUseCase { token ->
            get<SessionManagerPrefImpl>().saveToken(token)
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
    factory<GetLocationStoryUseCase> {
        GetLocationStoryUseCase {
            get<StoryRepository>().observeStoriesWithLocation()
        }
    }
}