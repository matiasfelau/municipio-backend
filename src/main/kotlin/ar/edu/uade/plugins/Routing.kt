package ar.edu.uade.plugins

import ar.edu.uade.routes.*
import ar.edu.uade.services.CredencialJWTService
import ar.edu.uade.services.CredencialService
import ar.edu.uade.services.EmpleadoJWTService
import ar.edu.uade.services.EmpleadoService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    credencialService: CredencialService,
    credencialJWTService: CredencialJWTService,
    empleadoService: EmpleadoService,
    empleadoJWTService: EmpleadoJWTService
    ) {
    routing {
        get("/") {
            call.respondText("")
        }
        credencialRouting(credencialService, credencialJWTService)
        empleadoRouting(empleadoService, empleadoJWTService)
    }
}
