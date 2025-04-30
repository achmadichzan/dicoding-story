package com.achmadichzan.dicodingstory

import android.app.Application
import com.achmadichzan.dicodingstory.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class DicodingStoryApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DicodingStoryApp)
            modules(appModule)
        }
    }
}