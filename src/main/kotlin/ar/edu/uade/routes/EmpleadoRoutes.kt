package ar.edu.uade.routes

import ar.edu.uade.models.Empleado
import ar.edu.uade.services.EmpleadoService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.util.*

fun Route.empleadoRouting(empleadoService: EmpleadoService) {
    route("/empleado") {

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
