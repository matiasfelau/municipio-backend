package ar.edu.uade.routes

import ar.edu.uade.mappers.responses.DesperfectoResponse
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
        var resultado: MutableList<DesperfectoResponse> = ArrayList<DesperfectoResponse>()
        try{
            val autenticacion = call.receive<Autenticacion>()
            if (jwtService.validateToken(autenticacion.token)) {
                call.response.status(HttpStatusCode.OK)
                val desperfectos = desperfectoService.getDesperfectos()
                for (desperfecto in desperfectos) {
                    resultado.add(desperfectoToResponse(desperfecto))
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

fun desperfectoToResponse(desperfecto: Desperfecto): DesperfectoResponse {
    return DesperfectoResponse(
        desperfecto.idDesperfecto,
        desperfecto.descripcion,
        desperfecto.idRubro
    )
}