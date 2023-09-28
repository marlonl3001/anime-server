package br.com.mdr.plugins

import br.com.mdr.routes.getAllHeroes
import br.com.mdr.routes.root
import br.com.mdr.routes.searchHeroes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        root()
        getAllHeroes()
        searchHeroes()

        //enable access to images resources when downloading image on app or by getting using image url
        staticResources("/images", "images")
    }
}
