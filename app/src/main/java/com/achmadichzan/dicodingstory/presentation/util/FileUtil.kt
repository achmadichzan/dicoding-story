package com.achmadichzan.dicodingstory.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtil {
    suspend fun from(context: Context, uri: Uri): File = withContext(Dispatchers.IO) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "upload-${System.currentTimeMillis()}.jpg")
        file.outputStream().use { inputStream?.copyTo(it) }
        file
    }

    suspend fun createImageFile(context: Context): File = withContext(Dispatchers.IO) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "JPEG_${timestamp}_"
        val storageDir = context.cacheDir
        File.createTempFile(fileName, ".jpg", storageDir)
    }

    suspend fun compressImageFileWithProgress(
        context: Context,
        file: File,
        maxSizeKB: Int = 1000,
        onProgress: (Float) -> Unit
    ): File = withContext(Dispatchers.IO) {
        val originalBitmap = BitmapFactory.decodeFile(file.path)
        val compressedFile = createImageFile(context)

        var quality = 100
        var streamLength: Int
        val targetSize = maxSizeKB * 1024f

        do {
            val stream = ByteArrayOutputStream()
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            val byteArray = stream.toByteArray()
            streamLength = byteArray.size
            quality -= 5

            val progress = (targetSize / streamLength).coerceAtMost(1f) * 100f
            onProgress(progress)

        } while (streamLength / 1024 > maxSizeKB && quality > 5)

        compressedFile.outputStream().use {
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, it)
        }

        compressedFile
    }
}