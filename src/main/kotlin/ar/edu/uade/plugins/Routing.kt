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
    vecinoService: VecinoService,
    jwtService: JWTService,
    reclamoService: ReclamoService,
    rubroService: RubroService,
    sitioService: SitioService
    ) {
    routing {
        get("/") {
            call.respondText("")
        }
        credencialRouting(credencialService, credencialJWTService)
        empleadoRouting(empleadoService, empleadoJWTService)
        vecinoRouting(vecinoService, credencialService)
        reclamoRouting(jwtService, reclamoService)
        rubroRouting(jwtService,rubroService)
        sitioRouting(jwtService,sitioService)
    }
}
