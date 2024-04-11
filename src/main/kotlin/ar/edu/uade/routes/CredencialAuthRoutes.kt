package ar.edu.uade.routes

import ar.edu.uade.mappers.CredencialRequest
import ar.edu.uade.services.CredencialJWTService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.credencialAuthRouting(credencialJwtService: CredencialJWTService) {
    post("/auth/vecino") {
        val dto = call.receive<CredencialRequest>()
        val token: String? = credencialJwtService.createJwtToken(dto)
        token?.let {
            call.respond(hashMapOf("token" to token))
        } ?: call.respond(
            message = HttpStatusCode.Unauthorized
        )
    }
}