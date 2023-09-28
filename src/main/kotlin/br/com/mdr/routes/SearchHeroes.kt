package br.com.mdr.routes

import br.com.mdr.repository.HeroRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.searchHeroes() {
    val repository: HeroRepository by inject()

    get("/heroes/search") {
        val name = call.request.queryParameters["name"]
        call.respond(
            message = repository.searchHeroes(name),
            status = HttpStatusCode.OK
        )
    }
}