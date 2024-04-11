package ar.edu.uade.routes

import ar.edu.uade.services.CredencialService
import io.ktor.server.routing.*

fun Route.credencialRouting(credencialService: CredencialService) {
    route("/vecino") {

    }
}