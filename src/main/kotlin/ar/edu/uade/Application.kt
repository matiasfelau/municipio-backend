package ar.edu.uade

import ar.edu.uade.plugins.*
import io.ktor.server.application.*
import ar.edu.uade.databases.MySQLSingleton
import ar.edu.uade.services.EmpleadoJWTService
import ar.edu.uade.services.EmpleadoService
import ar.edu.uade.services.CredencialJWTService
import ar.edu.uade.services.CredencialService

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    MySQLSingleton.init(environment.config)
    val empleadoService = EmpleadoService(environment.config)
    val empleadoJwtService = EmpleadoJWTService(this, empleadoService)
    val credencialService = CredencialService(environment.config)
    val credencialJWTService = CredencialJWTService(this, credencialService)
    configureRouting(empleadoService, empleadoJwtService, credencialService, credencialJWTService)
    configureSecurity(empleadoJwtService)
}
