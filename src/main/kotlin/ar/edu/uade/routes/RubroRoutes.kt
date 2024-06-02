package ar.edu.uade.routes

import ar.edu.uade.models.Rubro
import ar.edu.uade.services.*
import ar.edu.uade.utilities.Autenticacion
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.ArrayList

fun Route.rubroRouting(jwtService: JWTService, rubroService: RubroService) {
    val ruta = "/rubro"

    put("$ruta/todos"){
        var resultado: List<Rubro> = ArrayList<Rubro>()
        try{
            val body = call.receive<Autenticacion>()
            if (jwtService.validateToken(body.token)) {
                resultado = rubroService.getRubros()
            }else {
                call.response.status(HttpStatusCode.Unauthorized)
            }
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
        }

        call.respond(resultado)

    }

}