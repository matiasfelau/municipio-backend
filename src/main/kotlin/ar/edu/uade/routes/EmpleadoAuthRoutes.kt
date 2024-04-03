package ar.edu.uade.routes

import ar.edu.uade.mappers.EmpleadoLoginRequest
import ar.edu.uade.services.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.empleadoAuthRouting(empleadoJwtService: EmpleadoJWTService) {
    post {
        val dto = call.receive<EmpleadoLoginRequest>()
        val token: String? = empleadoJwtService.createJwtToken(dto)
        token?.let {
            call.respond(hashMapOf("token" to token))
        } ?: call.respond(
            message = HttpStatusCode.Unauthorized
        )
    }
}