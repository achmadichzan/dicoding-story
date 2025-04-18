package com.achmadichzan.dicodingstory.data.remote.extension

import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.http.headersOf
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import java.io.File

// currently unused
fun File.asMultipart(name: String, contentType: String): PartData.FileItem {
    return PartData.FileItem(
        provider = { this.inputStream().toByteReadChannel() },
        dispose = {},
        partHeaders = headersOf(
            HttpHeaders.ContentDisposition to
                    listOf("form-data; name=\"$name\"; filename=\"${this.name}\""),
            HttpHeaders.ContentType to listOf(contentType)
        )
    )
}