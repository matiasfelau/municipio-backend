package ar.edu.uade.routes

import ar.edu.uade.mappers.EmpleadoLoginRequest
import ar.edu.uade.mappers.EmpleadoResponse
import ar.edu.uade.models.Empleado
import ar.edu.uade.services.EmpleadoJWTService
import ar.edu.uade.services.EmpleadoService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.util.*

fun Route.empleadoRouting(empleadoService: EmpleadoService, empleadoJWTService: EmpleadoJWTService) {
    route("/empleado") {
        get {
            val empleado = call.receive<EmpleadoLoginRequest>().let { empleadoService.findEmpleadoByLegajo(it) }
        }
    }
}
/*
private fun EmpleadoRequest.toModel(): Empleado =
    Empleado(
        legajo = this.legajo,
        password = this.password
    )
private fun Empleado.toResponse(): EmpleadoResponse =
    EmpleadoResponse(
        id = this.id,
        username = this.username,
    )

 */