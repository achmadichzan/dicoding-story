package com.achmadichzan.dicodingstory

import android.app.Application
import com.achmadichzan.dicodingstory.di.appModule
import com.achmadichzan.dicodingstory.data.local.preferences.UserPreferencesImpl
import com.achmadichzan.dicodingstory.presentation.util.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

        CoroutineScope(Dispatchers.Default).launch {
            val pref = UserPreferencesImpl(this@DicodingStoryApp)
            SessionManager.token = pref.getToken().toString()
        }
    }
}