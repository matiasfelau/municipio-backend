package ar.edu.uade.routes


import ar.edu.uade.mappers.responses.SitioResponse
import ar.edu.uade.models.Sitio

import ar.edu.uade.services.JWTService
import ar.edu.uade.services.SitioService
import ar.edu.uade.utilities.Autenticacion
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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
}

private fun sitioToResponse(sitio: Sitio): SitioResponse {
    return SitioResponse(
        sitio.idSitio,
        sitio.descripcion
    )
}