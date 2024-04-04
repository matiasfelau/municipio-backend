package ar.edu.uade.routes

import ar.edu.uade.services.VecinoService
import io.ktor.server.routing.*

fun Route.vecinoRouting(vecinoService: VecinoService) {
    route("/vecino") {

    }
}