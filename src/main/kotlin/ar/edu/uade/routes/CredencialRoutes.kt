package ar.edu.uade.routes

import ar.edu.uade.mappers.CredencialRequest
import ar.edu.uade.models.Credencial
import ar.edu.uade.models.Vecino
import ar.edu.uade.services.CredencialJWTService
import ar.edu.uade.services.CredencialService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.credencialRouting(credencialService: CredencialService, credencialJwtService: CredencialJWTService) {

    post("/credenciales/solicitar") {
        try {
            val request = call.receive<CredencialRequest>()
            if (credencialService.solicitarCredencial(request.documento, request.email)) {
                call.respond(message = HttpStatusCode.Created)
            } else {
                call.respond(message = HttpStatusCode.BadRequest)
            }
        } catch (e: Exception) {
            call.respond(message = HttpStatusCode.InternalServerError)
        }
    }

    put("/credenciales/habilitar") {
        try {
            val request = call.receive<CredencialRequest>()
            val bd = credencialService.find(request.documento)
            if (bd != null) {
                credencialService.habilitarCredencial(bd)
                call.respond(message = HttpStatusCode.OK)
            } else {
                call.respond(message = HttpStatusCode.BadRequest)
            }
        } catch (e: Exception) {
            call.respond(message = HttpStatusCode.InternalServerError)
        }
    }

    put("/credenciales/modificar") {
        try {
            val request = call.receive<CredencialRequest>()
            val bd = credencialService.find(request.documento)
            if (bd != null) {
                credencialService.clavePrimerIngresoCredencial(bd)
                call.respond(message = HttpStatusCode.OK)
            } else {
                call.respond(message = HttpStatusCode.BadRequest)
            }
        } catch (e: Exception) {
            call.respond(message = HttpStatusCode.InternalServerError)
        }
    }

    get("/credenciales/ingresar") {
        try {
            val request = call.receive<CredencialRequest>()
            val token: String? = credencialJwtService.createJwtToken(request)
            token?.let {
                call.respond(hashMapOf("token" to token))
            } ?: call.respond(
                message = HttpStatusCode.Unauthorized
            )
        } catch (e: Exception) {
            call.respond(message = HttpStatusCode.InternalServerError)
        }
    }

    get("/credenciales") {
        try {
            val request = call.receive<CredencialRequest>()
            val bd = credencialService.find(request.documento)
            if (bd != null) {
                call.respond(credencialToResponse(bd))
            } else {
                call.respond(message = HttpStatusCode.BadRequest)
            }
        } catch (e: Exception) {
            call.respond(message = HttpStatusCode.InternalServerError)
        }
    }

fun credencialToResponse(credencial: Credencial): CredencialResponse {
    return CredencialResponse()
}