package com.achmadichzan.dicodingstory.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
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

    fun compressImageFile(context: Context, file: File, maxSizeKB: Int = 1000): File {
        val originalBitmap = BitmapFactory.decodeFile(file.path)
        val compressedFile = createImageFile(context)

        var quality = 100
        var streamLength: Int

        do {
            val stream = ByteArrayOutputStream()
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            val byteArray = stream.toByteArray()
            streamLength = byteArray.size
            quality -= 5
        } while (streamLength / 1024 > maxSizeKB && quality > 5)

        compressedFile.outputStream().use {
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, it)
        }

        return compressedFile
    }

}