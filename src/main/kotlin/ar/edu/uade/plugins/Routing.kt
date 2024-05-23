package ar.edu.uade.plugins

import ar.edu.uade.routes.*
import ar.edu.uade.services.EmpleadoJWTService
import ar.edu.uade.services.EmpleadoService
import ar.edu.uade.services.CredencialJWTService
import ar.edu.uade.services.CredencialService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    empleadoService: EmpleadoService,
    empleadoJWTService: EmpleadoJWTService,
    credencialService: CredencialService,
    credencialJWTService: CredencialJWTService
    ) {
    /*
    val empleadoDAO: EmpleadoDAOFacade = EmpleadoDAOFacadeCacheImpl(
        EmpleadoDAOFacadeImpl(),
        File(environment.config.property("storage.ehcacheFilePath").getString())
    ).apply { }

     */
    routing {
        get("/") {
            call.respondText("")
        }
        empleadoRouting(empleadoService)
        empleadoAuthRouting(empleadoJWTService)
        credencialRouting(credencialService, credencialJWTService)
    }
}
