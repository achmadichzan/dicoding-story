package com.achmadichzan.dicodingstory.di

import com.achmadichzan.dicodingstory.presentation.viewmodel.DetailViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.LoginViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.MapsLocationViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.RegisterViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.StoryViewModel
import com.achmadichzan.dicodingstory.presentation.viewmodel.UploadViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { StoryViewModel(get(), get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { UploadViewModel(get()) }
    viewModel { MapsLocationViewModel(get()) }
}