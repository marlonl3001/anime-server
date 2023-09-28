package br.com.mdr.plugins

import br.com.mdr.routes.getAllHeroes
import br.com.mdr.routes.root
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        root()
        getAllHeroes()
    }
}
