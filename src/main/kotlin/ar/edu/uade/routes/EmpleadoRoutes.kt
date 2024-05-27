package ar.edu.uade.routes

import ar.edu.uade.mappers.requests.CredencialRequest
import ar.edu.uade.mappers.requests.EmpleadoRequest
import ar.edu.uade.services.EmpleadoJWTService
import ar.edu.uade.services.EmpleadoService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.empleadoRouting(empleadoService: EmpleadoService, empleadoJWTService: EmpleadoJWTService) {

    post("/empleado/ingreso") {
        try {
            val request = call.receive<EmpleadoRequest>()
            val token: String? = empleadoJWTService.createJwtToken(request)
            token?.let {
                call.respond(hashMapOf("token" to token))
            } ?: call.respond(message = HttpStatusCode.Unauthorized)

        } catch (e: Exception) {
            call.respond(message = HttpStatusCode.InternalServerError)
        }
    }
}

/*
private fun EmpleadoRequest.toModel(): Empleado =
    Empleado(
        legajo = this.legajo,
        nombre = this.nombre,
        apellido = this.apellido,
        password = this.password,
        sector = this.sector,
        categoria = this.categoria,
        fechaIngreso = this.fechaIngreso
    )

 */
/*
private fun Empleado.toResponse(): EmpleadoResponse =
    EmpleadoResponse(
        id = this.id,
        username = this.username,
    )
 */