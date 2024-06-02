package ar.edu.uade.routes

import ar.edu.uade.models.Desperfecto
import ar.edu.uade.services.*
import ar.edu.uade.utilities.Autenticacion
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.ArrayList

fun Route.desperfectoRouting(jwtService: JWTService, desperfectoService: DesperfectoService) {
    val ruta = "/desperfecto"

    put("$ruta/todos"){
        var resultado: List<Desperfecto> = ArrayList<Desperfecto>()
        try{
            val body = call.receive<Autenticacion>()
            if (jwtService.validateToken(body.token)) {
                resultado = desperfectoService.getDesperfectos()
            }else {
                call.response.status(HttpStatusCode.Unauthorized)
            }
        }catch (nullPointerException: NullPointerException) {
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

}