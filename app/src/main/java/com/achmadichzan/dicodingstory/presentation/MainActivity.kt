package com.achmadichzan.dicodingstory.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.achmadichzan.dicodingstory.presentation.navigation.NavMain
import com.achmadichzan.dicodingstory.presentation.theme.DicodingStoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DicodingStoryTheme {
                NavMain()
            }
        }
    }
}