package ar.edu.uade.routes

import ar.edu.uade.mappers.requests.CredencialRequest
import ar.edu.uade.mappers.responses.CredencialResponse
import ar.edu.uade.models.Credencial
import ar.edu.uade.services.CredencialJWTService
import ar.edu.uade.services.CredencialService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.credencialRouting(credencialService: CredencialService, credencialJwtService: CredencialJWTService) {

    post("/vecino/registro") {
        try {
            val request = call.receive<CredencialRequest>()
            val boolean = credencialService.solicitarCredencial(request.documento, request.email);
            call.response.status(HttpStatusCode.Created)
            call.respond(boolean)
        } catch (e: Exception) {
            call.respond(false)
        }
    }

    get("/vecino/habilitacion/{documento}") {
        var bool = false
        try {
            val documento = call.parameters["documento"]
            val bd = documento?.let { d -> credencialService.find(d) }
            if (bd != null) {
                bool = credencialService.habilitarCredencial(bd)
                call.respond(bool)
            } else {
                call.respond(bool)
            }
        } catch (e: Exception) {
            call.respond(bool)
        }
    }

    put("/vecino/primer-ingreso") {
        try {
            val request = call.receive<CredencialRequest>()
            credencialService.casoPrimerIngresoCredencial(request)
            call.respond(true)
        } catch (e: Exception) {
            call.respond(false)
        }
    }

    post("/vecino/ingreso") {
        try {
            val request = call.receive<CredencialRequest>()
            val token: String? = credencialJwtService.createJwtToken(request)
            token?.let {
                call.respond(hashMapOf("token" to token))
            } ?: call.respond(message = HttpStatusCode.Unauthorized)

        } catch (e: Exception) {
            call.respond(message = HttpStatusCode.InternalServerError)
        }
    }

    get("/vecino/confirmar-primer-ingreso/{documento}") {
        try {
            val documento = call.parameters["documento"]
            val bd = documento?.let { d -> credencialService.find(d) }
            if (bd != null) {
                call.respond(bd.primerIngreso)
            } else {
                call.respond(false)
            }
        } catch (e: Exception) {
            call.respond(false)
        }
    }

    put("/vecino/recuperacion") {
        var bool = false
        try {
            val request = call.receive<CredencialRequest>()
            val bd = credencialService.find(request.documento)
            if (bd != null) {
                if (request.email == bd.email) {
                    bool = credencialService.recuperarCredencial(bd)
                    call.respond(bool)
                } else {
                    call.respond(false)
                }
            } else {
                call.respond(bool)
            }
        } catch (e: Exception) {
            call.respond(bool)
        }
    }
}

fun credencialToResponse(credencial: Credencial): CredencialResponse {
    return CredencialResponse(
        credencial.documento,
        credencial.email,
        credencial.primerIngreso
    )
}
