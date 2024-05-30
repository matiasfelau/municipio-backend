package ar.edu.uade

import ar.edu.uade.plugins.*
import io.ktor.server.application.*
import ar.edu.uade.databases.MySQLSingleton
import ar.edu.uade.services.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    MySQLSingleton.init(environment.config)
    val credencialService = CredencialService(environment.config)
    val credencialJWTService = CredencialJWTService(this, credencialService)
    val empleadoService = EmpleadoService(environment.config)
    val empleadoJWTService = EmpleadoJWTService(this, empleadoService)
    val vecinoService = VecinoService(environment.config)
    val jwtService = JWTService(this)
    val reclamoService = ReclamoService(environment.config)
    configureSecurity(credencialJWTService, empleadoJWTService)
    configureRouting(credencialService, credencialJWTService, empleadoService, empleadoJWTService, vecinoService, jwtService, reclamoService)

}
