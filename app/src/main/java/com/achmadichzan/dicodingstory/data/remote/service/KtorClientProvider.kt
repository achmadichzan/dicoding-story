package com.achmadichzan.dicodingstory.data.remote.service

import android.util.Log
import com.achmadichzan.dicodingstory.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun provideHttpClient(): HttpClient {
    return HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
//            logger = Logger.DEFAULT
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("Ktor logger", "log: $message")
                }
            }
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = BuildConfig.BASE_URL
                path("v1") // doesnt work
            }
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }
}
