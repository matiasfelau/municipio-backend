package ar.edu.uade.routes

import ar.edu.uade.mappers.requests.ReclamoRequest
import ar.edu.uade.mappers.responses.ReclamoResponse
import ar.edu.uade.models.Reclamo
import ar.edu.uade.services.JWTService
import ar.edu.uade.services.ReclamoService
import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.containers.AutenticacionFiltro
import ar.edu.uade.utilities.containers.AutenticacionReclamo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.ArrayList

fun Route.reclamoRouting(jwtService: JWTService, reclamoService: ReclamoService) {
    val ruta = "/reclamo"

    put("$ruta/todos/{pagina}") {
        var resultado: List<Reclamo> = ArrayList<Reclamo>()
        try {
            val pagina = call.parameters["pagina"]!!.toInt()
            val body = call.receive<AutenticacionFiltro>()
            val autenticacion = body.autenticacion
            val filtro = body.filtro
            if (jwtService.validateToken(autenticacion.token)) {
                if ((autenticacion.tipo == "vecino") || (autenticacion.tipo == "empleado" && filtro.tipo == "sector")) {
                    resultado = reclamoService.getReclamosByFiltro(pagina, filtro)
                    call.response.status(HttpStatusCode.OK)
                } else {
                    call.response.status(HttpStatusCode.Forbidden)
                }
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
            }
        } catch (nullPointerException: NullPointerException) {
            call.response.status(HttpStatusCode.BadRequest)
        } catch (numberFormatException: NumberFormatException) {
            call.response.status(HttpStatusCode.BadRequest)
        } catch (noSuchMethodException: NoSuchMethodException) {
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

    post("$ruta/nueva"){
        try{
            var request = call.receive<AutenticacionReclamo>()
            val autenticacion = request.autenticacion
            val reclamoRQ = request.reclamo
            if (jwtService.validateToken(autenticacion.token)) {
                reclamoService.createReclamo(requestToReclamo(reclamoRQ))
                call.response.status(HttpStatusCode.Created)
            }else {
                call.response.status(HttpStatusCode.Unauthorized)
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
        }

    }
}

private fun requestToReclamo(reclamoRQ: ReclamoRequest): Reclamo {
    return Reclamo(
        null,
        reclamoRQ.descripcion,
        reclamoRQ.estado,
        reclamoRQ.documento,
        reclamoRQ.idSitio,
        reclamoRQ.idDesperfecto,
        null
    )
}

private fun reclamoToResponse(reclamo: Reclamo): ReclamoResponse {
    return ReclamoResponse(
        reclamo.idReclamo,
        reclamo.descripcion,
        reclamo.estado,
        reclamo.documento
    )
}