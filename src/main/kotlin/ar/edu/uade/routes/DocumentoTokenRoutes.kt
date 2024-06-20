package ar.edu.uade.routes

import ar.edu.uade.mappers.requests.DocumentoTokenRequest
import ar.edu.uade.mappers.requests.EmpleadoRequest
import ar.edu.uade.mappers.requests.ReclamoRequest
import ar.edu.uade.models.DocumentoToken
import ar.edu.uade.models.Reclamo
import ar.edu.uade.services.DocumentoTokenService
import ar.edu.uade.services.EmpleadoJWTService
import ar.edu.uade.services.EmpleadoService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.documentoTokenRouting(documentoTokenService: DocumentoTokenService) {
    post("/notificacion/token") {
        try {
            documentoTokenService.newToken(requestToDocumentoToken(call.receive<DocumentoTokenRequest>()))
            call.response.status(HttpStatusCode.OK)
        }
        catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
        }
    }
}

private fun requestToDocumentoToken(request: DocumentoTokenRequest): DocumentoToken {
    return DocumentoToken(
        request.documento,
        request.token
    )
}