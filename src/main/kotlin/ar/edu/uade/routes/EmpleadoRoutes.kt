package ar.edu.uade.routes

import ar.edu.uade.mappers.requests.EmpleadoRequest
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