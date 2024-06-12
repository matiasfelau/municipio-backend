package ar.edu.uade.routes

import ar.edu.uade.mappers.requests.CredencialRequest
import ar.edu.uade.mappers.requests.EmpleadoRequest
import ar.edu.uade.mappers.responses.EmpleadoResponse
import ar.edu.uade.mappers.responses.VecinoResponse
import ar.edu.uade.models.Credencial
import ar.edu.uade.models.Empleado
import ar.edu.uade.models.Vecino
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
                call.response.status(HttpStatusCode.OK)
                call.respond(hashMapOf("token" to token))
            } ?: call.respond(message = HttpStatusCode.Unauthorized)

        } catch (e: Exception) {
            call.respond(message = HttpStatusCode.InternalServerError)
        }
    }

    get("/empleado/perfil/{legajo}") {
        try {
            val legajo = Integer.parseInt(call.parameters["legajo"])
            val db = empleadoService.findEmpleadoByLegajo(legajo)
            db?.let { it1 -> empleadoToResponse(it1) }?.let { it2 -> call.respond(it2) }
        } catch (_: Exception) {

        }
    }


}

private fun empleadoToResponse(empleado: Empleado): EmpleadoResponse {
    return EmpleadoResponse(
        empleado.legajo,
        empleado.nombre,
        empleado.apellido,
        empleado.password,
        empleado.sector
    )
}

