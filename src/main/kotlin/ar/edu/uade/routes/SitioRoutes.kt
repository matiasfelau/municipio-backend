package ar.edu.uade.routes


import ar.edu.uade.mappers.requests.SitioRequest
import ar.edu.uade.mappers.responses.SitioResponse
import ar.edu.uade.mappers.responses.SitioResponse2
import ar.edu.uade.models.Reclamo
import ar.edu.uade.models.Sitio

import ar.edu.uade.services.JWTService
import ar.edu.uade.services.SitioService
import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.containers.AutenticacionReclamo
import ar.edu.uade.utilities.containers.AutenticacionSitio
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.time.LocalTime
import java.util.ArrayList

fun Route.sitioRouting(jwtService: JWTService, sitioService: SitioService) {
    val ruta = "/sitio"

    put("$ruta/todos"){
        val resultado: MutableList<SitioResponse> = ArrayList<SitioResponse>()
        try{
            val autenticacion = call.receive<Autenticacion>()
            if (jwtService.validateToken(autenticacion.token)) {
                val sitios = sitioService.getSitios()
                for (sitio in sitios) {
                    resultado.add(sitioToResponse(sitio))
                }
                println("\n--------------------" +
                        "\nSTATUS:OK" +
                        "\n--------------------" +
                        "\nUSUARIO:${autenticacion.tipo}" +
                        "\nCANTIDAD DE SITIOS:${resultado.size}" +
                        "\n--------------------")
            }else {
                call.response.status(HttpStatusCode.Unauthorized)
            }
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
        }
        call.respond(resultado)

    }

    post("$ruta/nuevo"){
        var result: Int = 0
        try{
            val request = call.receive<AutenticacionSitio>()
            val autenticacion = request.autenticacion
            val sitio = request.sitio
            if (jwtService.validateToken(autenticacion.token)) {
                result = sitioService.addSitio(requestToSitio(sitio))!!
                call.response.status(HttpStatusCode.Created)
                if (result != null) {
                    println("EL ID DEL SITIO ES:${result}")
                }
                if (result != null) {
                    println("\n--------------------" +
                            "\nSTATUS:SITIO CREATED" +
                            "\n--------------------")
                }

            }else {
                call.response.status(HttpStatusCode.Unauthorized)
                println("\n--------------------" +
                        "\nSTATUS:SITIO UNAUTHORIZED" +
                        "\n--------------------")
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            println("\n--------------------" +
                    "\nSTATUS:SITIO BAD REQUEST" +
                    "\n--------------------")
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println("\n--------------------" +
                    "\nSTATUS:SITIO INTERNAL SERVER ERROR" +
                    "\n--------------------")
            exception.printStackTrace()
        }
        call.respond(result)
    }

    get("$ruta/particular"+"/{idSitio}"){
        try{
            val idSitio = call.parameters["idSitio"]?.toIntOrNull()
            if (idSitio != null) {
                val encontrado = sitioService.getSitio(idSitio)
                if (encontrado != null) {
                    val resultado = sitioToResponse2(encontrado)
                    call.response.status(HttpStatusCode.OK)
                    call.respond(resultado)
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            } else {
                call.response.status(HttpStatusCode.BadRequest)
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            println("\n--------------------" +
                    "\nSTATUS:SITIO BAD REQUEST" +
                    "\n--------------------")
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println("\n--------------------" +
                    "\nSTATUS:SITIO INTERNAL SERVER ERROR" +
                    "\n--------------------")
            exception.printStackTrace()
        }
    }
}

private fun requestToSitio(sitio: SitioRequest): Sitio {
    return Sitio(
        null,
        sitio.latitud,
        sitio.longitud,
        null,
        0,
        null,
        null,
        sitio.descripcion,
        "",
        LocalTime.now(),
        LocalTime.now(),
        ""
    )
}

private fun sitioToResponse(sitio: Sitio): SitioResponse {
    return SitioResponse(
        sitio.idSitio!!,
        sitio.descripcion
    )
}

private fun sitioToResponse2(sitio: Sitio): SitioResponse2 {
    return SitioResponse2(
        sitio.idSitio,
        sitio.latitud,
        sitio.longitud
    )
}