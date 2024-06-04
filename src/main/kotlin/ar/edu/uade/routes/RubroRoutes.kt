package ar.edu.uade.routes

import ar.edu.uade.mappers.responses.RubroResponse
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
        var resultado: MutableList<RubroResponse> = ArrayList<RubroResponse>()
        try{
            val autenticacion = call.receive<Autenticacion>()
            if (jwtService.validateToken(autenticacion.token)) {
                call.response.status(HttpStatusCode.OK)
                val rubros = rubroService.getRubros()
                for (rubro in rubros) {
                    resultado.add(rubroToResponse(rubro))
                }
                println("\n--------------------" +
                        "\nSTATUS:OK" +
                        "\n--------------------" +
                        "\nUSUARIO:${autenticacion.tipo}" +
                        "\nCANTIDAD DE RUBROS:${resultado.size}" +
                        "\n--------------------")
            }else {
                call.response.status(HttpStatusCode.Unauthorized)
                println("\n--------------------" +
                        "\nSTATUS:UNAUTHORIZED" +
                        "\n--------------------" +
                        "\nUSUARIO:${autenticacion.tipo}"+
                        "\n--------------------")
            }
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println("\n--------------------" +
                    "\nSTATUS:INTERNAL SERVER ERROR" +
                    "\n--------------------")
        }

        call.respond(resultado)

    }

}

fun rubroToResponse(rubro: Rubro): RubroResponse {
    return RubroResponse(
        rubro.idRubro,
        rubro.descripcion
    )
}