package br.com.mdr.plugins

import io.ktor.http.HttpHeaders
import io.ktor.server.application.*
import io.ktor.server.plugins.defaultheaders.*
import java.time.Duration

fun Application.configureDefaultHeader() {
    install(DefaultHeaders) {
        //Responsible for caching images, allowing app download images even when server is down
        val oneYearInSeconds = Duration.ofDays(365).seconds
        header(HttpHeaders.CacheControl, "public, max-age=$oneYearInSeconds, immutable")
    }
}