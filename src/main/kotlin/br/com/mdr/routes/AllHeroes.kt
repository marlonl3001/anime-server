package br.com.mdr.routes

import br.com.mdr.models.ApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.lang.IllegalArgumentException

private const val DEFAULT_PAGE = 1

fun Route.getAllHeroes() {
    get("/heroes") {
        try {
            val page = call.request.queryParameters["page"]?.toInt() ?: DEFAULT_PAGE
            //Define the range of pages available to send with response
            require(page in 1..5)

            call.respond(message = page)
        } catch (e: NumberFormatException) {
            call.respond(
                message = ApiResponse(success = false, message = "Only numbers allowed."),
                status = HttpStatusCode.BadRequest
            )
        } catch (e: IllegalArgumentException) {
            call.respond(
                message = ApiResponse(success = false, message = "Heroes not found."),
                status = HttpStatusCode.BadRequest
            )
        }

    }
}