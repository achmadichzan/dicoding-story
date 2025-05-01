package com.achmadichzan.dicodingstory.presentation.state

import android.net.Uri
import java.io.File

data class UploadState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null,
    val isLocationEnabled: Boolean = false,
    val selectedFile: File? = null,
    val imageUri: Uri? = null,
    val isCompressing: Boolean = false,
    val compressingProgress: Float = 0f
)