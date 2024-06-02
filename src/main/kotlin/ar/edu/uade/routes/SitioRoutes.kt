package ar.edu.uade.routes


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
    val ruta = "/sitios"

    put("$ruta/todos"){
        var resultado: List<Sitio> = ArrayList<Sitio>()
        try{
            val body = call.receive<Autenticacion>()
            if (jwtService.validateToken(body.token)) {
                resultado = sitioService.getSitios()
            }else {
                call.response.status(HttpStatusCode.Unauthorized)
            }
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
        }
        call.respond(resultado)

    }
}