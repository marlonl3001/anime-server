package br.com.mdr.routes

import br.com.mdr.models.ApiResponse
import br.com.mdr.repository.HeroRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.lang.IllegalArgumentException

private const val DEFAULT_PAGE = 1
private const val DEFAULT_LIMIT = 5

fun Route.getAllHeroes() {
    val repository: HeroRepository by inject()

    get("/heroes") {
        try {
            val page = call.request.queryParameters["page"]?.toInt() ?: DEFAULT_PAGE
            val limit = call.request.queryParameters["limit"]?.toInt() ?: DEFAULT_LIMIT

            val apiResponse = repository.getAllHeroes(
                actualPage = page,
                limit = limit
            )

            call.respond(
                message = apiResponse,
                status = HttpStatusCode.OK
            )
        } catch (e: NumberFormatException) {
            call.respond(
                message = ApiResponse(success = false, message = "Only numbers allowed."),
                status = HttpStatusCode.BadRequest
            )
        } catch (e: IllegalArgumentException) {
            call.respond(
                message = ApiResponse(success = false, message = "Heroes not found."),
                status = HttpStatusCode.NotFound
            )
        }

    }
}