package ar.edu.uade.routes

import ar.edu.uade.mappers.responses.ReclamoResponse
import ar.edu.uade.models.Reclamo
import ar.edu.uade.services.JWTService
import ar.edu.uade.services.ReclamoService
import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.containers.AutenticacionFiltro
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.ArrayList


fun Route.reclamoRouting(jwtService: JWTService, reclamoService: ReclamoService) {
    val ruta = "/reclamo"

    put("$ruta/todos/{pagina}") {
        val resultado: MutableList<ReclamoResponse> = ArrayList<ReclamoResponse>()
        try {
            val pagina = call.parameters["pagina"]!!.toInt()
            val body = call.receive<AutenticacionFiltro>()
            val autenticacion = body.autenticacion
            val filtro = body.filtro
            if (jwtService.validateToken(autenticacion.token)) {
                if ((autenticacion.tipo == "Vecino") || (autenticacion.tipo == "Empleado" && filtro.tipo == "Sector")) {
                    println(filtro.tipo)
                    println(filtro.dato)
                    val reclamos = reclamoService.getReclamosByFiltro(pagina, filtro)
                    for (reclamo in reclamos) {
                        resultado.add(reclamoToResponse(reclamo))
                        println(reclamo.documento)
                    }
                    call.response.status(HttpStatusCode.OK)
                } else {
                    call.response.status(HttpStatusCode.Forbidden)
                }
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
            }
        } catch (exposedSQLException: ExposedSQLException) {
            println(exposedSQLException.message)
            call.response.status(HttpStatusCode.BadRequest)
        } catch (nullPointerException: NullPointerException) {
            println(nullPointerException.message)
            call.response.status(HttpStatusCode.BadRequest)
        } catch (numberFormatException: NumberFormatException) {
            println(numberFormatException.message)
            call.response.status(HttpStatusCode.BadRequest)
        } catch (noSuchMethodException: NoSuchMethodException) {
            println(noSuchMethodException.message)
            call.response.status(HttpStatusCode.BadRequest)
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
        }
        call.respond(resultado)
    }

    put("$ruta/particular/{id}") {
        var resultado: ReclamoResponse = ReclamoResponse(null, null, null, null)
        try {
            val id = call.parameters["id"]!!.toInt()
            val autenticacion = call.receive<Autenticacion>()
            if (jwtService.validateToken(autenticacion.token)) {
                val reclamo = reclamoService.getReclamoById(id)
                if (reclamo != null) {
                    resultado = reclamoToResponse(reclamo)
                    call.response.status(HttpStatusCode.OK)
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
            }
        } catch (nullPointerException: NullPointerException) {
            call.response.status(HttpStatusCode.BadRequest)
        } catch (numberFormatException: NumberFormatException) {
            call.response.status(HttpStatusCode.BadRequest)
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
        }
        call.respond(resultado)
    }
}

private fun reclamoToResponse(reclamo: Reclamo): ReclamoResponse {
    return ReclamoResponse(
        reclamo.idReclamo,
        reclamo.descripcion,
        reclamo.estado,
        reclamo.documento
    )
}