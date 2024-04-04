package ar.edu.uade.routes

import ar.edu.uade.mappers.VecinoRequest
import ar.edu.uade.services.VecinoJWTService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.vecinoAuthRouting(vecinoJwtService: VecinoJWTService) {
    post("/auth/vecino") {
        val dto = call.receive<VecinoRequest>()
        val token: String? = vecinoJwtService.createJwtToken(dto)
        token?.let {
            call.respond(hashMapOf("token" to token))
        } ?: call.respond(
            message = HttpStatusCode.Unauthorized
        )
    }
}