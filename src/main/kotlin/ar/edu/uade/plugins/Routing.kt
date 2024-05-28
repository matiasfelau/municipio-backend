package ar.edu.uade.plugins

import ar.edu.uade.routes.*
import ar.edu.uade.services.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    credencialService: CredencialService,
    credencialJWTService: CredencialJWTService,
    empleadoService: EmpleadoService,
    empleadoJWTService: EmpleadoJWTService,
    vecinoService: VecinoService
    ) {
    routing {
        get("/") {
            call.respondText("")
        }
        credencialRouting(credencialService, credencialJWTService)
        empleadoRouting(empleadoService, empleadoJWTService)
        vecinoRouting(vecinoService, credencialService)
    }
}
