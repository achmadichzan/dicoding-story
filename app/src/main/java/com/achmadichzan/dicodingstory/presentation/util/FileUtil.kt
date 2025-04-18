package com.achmadichzan.dicodingstory.presentation.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtil {
    fun from(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "upload-${System.currentTimeMillis()}.jpg")
        file.outputStream().use { inputStream?.copyTo(it) }
        return file
    }

    fun createImageFile(context: Context): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "JPEG_${timestamp}_"
        val storageDir = context.cacheDir
        return File.createTempFile(fileName, ".jpg", storageDir)
    }
}