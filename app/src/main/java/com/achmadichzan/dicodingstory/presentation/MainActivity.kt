package com.achmadichzan.dicodingstory.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.achmadichzan.dicodingstory.presentation.navigation.NavMain
import com.achmadichzan.dicodingstory.presentation.theme.DicodingStoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DicodingStoryTheme {
                Surface {
                    NavMain()
                }
            }
        }
    }
}