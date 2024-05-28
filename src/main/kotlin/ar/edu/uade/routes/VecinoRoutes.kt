package ar.edu.uade.routes

import ar.edu.uade.mappers.responses.VecinoResponse
import ar.edu.uade.models.Credencial
import ar.edu.uade.models.Vecino
import ar.edu.uade.services.CredencialService
import ar.edu.uade.services.VecinoService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.vecinoRouting(vecinoService: VecinoService, credencialService: CredencialService) {

    get("/vecino/perfil/{documento}") {
        try {
            val documento = call.parameters["documento"]
            val dbVecino = documento?.let { d -> vecinoService.getVecino(d) }
            val dbCredencial = documento?.let { d -> credencialService.find(d) }
            if (dbVecino != null) {
                dbCredencial?.let { cred -> vecinoToResponse(dbVecino, cred) }?.let { vec -> call.respond(vec) }
            }
        } catch (_: Exception) {

        }
    }
}

private fun vecinoToResponse(vecino: Vecino, credencial: Credencial): VecinoResponse? {
    return credencial.password?.let {
        VecinoResponse(
        vecino.nombre,
        vecino.apellido,
        vecino.documento,
        credencial.email,
            it
    )
    }
}