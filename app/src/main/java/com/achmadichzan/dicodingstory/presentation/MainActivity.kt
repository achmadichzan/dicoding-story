package com.achmadichzan.dicodingstory.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.achmadichzan.dicodingstory.domain.preferences.SessionManagerPrefImpl
import com.achmadichzan.dicodingstory.presentation.navigation.NavMain
import com.achmadichzan.dicodingstory.presentation.ui.theme.DicodingStoryTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var userPreferences: SessionManagerPrefImpl
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userPreferences = SessionManagerPrefImpl(applicationContext)

        var isTokenLoaded = false

        splashScreen.setKeepOnScreenCondition { !isTokenLoaded }

        lifecycleScope.launch {
            token = userPreferences.getToken()
            isTokenLoaded = true

            setContent {
                DicodingStoryTheme {
                    Surface {
                        NavMain(token = token)
                    }
                }
            }
        }
    }
}