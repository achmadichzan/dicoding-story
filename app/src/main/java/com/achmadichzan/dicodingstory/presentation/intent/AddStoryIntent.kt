package com.achmadichzan.dicodingstory.presentation.intent

import android.content.Context
import android.net.Uri

sealed class AddStoryIntent {
    data object GoBack : AddStoryIntent()
    data class ToggleLocation(val enabled: Boolean) : AddStoryIntent()
    data class PickImage(val uri: Uri, val context: Context) : AddStoryIntent()
}