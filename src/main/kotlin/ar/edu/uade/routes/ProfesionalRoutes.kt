package ar.edu.uade.routes

import ar.edu.uade.mappers.responses.ProfesionalResponse
import ar.edu.uade.models.Profesional
import ar.edu.uade.services.JWTService
import ar.edu.uade.services.ProfesionalService
import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.CloudinaryConfig
import ar.edu.uade.utilities.containers.AutenticacionProfesional
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Route.profesionalRouting(profesionalService: ProfesionalService, jwtService: JWTService, cloudinaryConfig: CloudinaryConfig) {

    val API_ROUTE: String = "/profesionales"

    put("$API_ROUTE/cantidad-paginas") {
        var resultado: Int = 0
        try {
            val autenticacion = call.receive<Autenticacion>()
            if (jwtService.validateToken(autenticacion.token)) {
                if (autenticacion.tipo == "VECINO") {
                    resultado = profesionalService.getCantidadPaginas()
                    println("\n--------------------" +
                            "\nACTOR:CANTIDAD_PAGINAS_PROFESIONALES" +
                            "\nSTATUS:OK" +
                            "\n--------------------" +
                            "\nUSUARIO:${autenticacion.tipo}" +
                            "\nTOKEN:${autenticacion.token}" +
                            "\n--------------------" +
                            "\nRESULTADO:${resultado}" +
                            "\n--------------------"
                    )
                    call.response.status(HttpStatusCode.OK)
                } else {
                    println("\n--------------------" +
                            "\nACTOR:CANTIDAD_PAGINAS_PROFESIONALES" +
                            "\nSTATUS:FORBIDDEN" +
                            "\n--------------------" +
                            "\nUSUARIO:${autenticacion.tipo}" +
                            "\nTOKEN:${autenticacion.token}" +
                            "\n--------------------" +
                            "\nRESULTADO:${resultado}" +
                            "\n--------------------"
                    )
                    call.response.status(HttpStatusCode.Forbidden)
                }
            } else {
                println("\n--------------------" +
                        "\nACTOR:CANTIDAD_PAGINAS_PROFESIONALES" +
                        "\nSTATUS:UNAUTHORIZED" +
                        "\n--------------------" +
                        "\nUSUARIO:${autenticacion.tipo}" +
                        "\nTOKEN:${autenticacion.token}" +
                        "\n--------------------" +
                        "\nRESULTADO:${resultado}" +
                        "\n--------------------"
                )
                call.response.status(HttpStatusCode.Unauthorized)
            }
        } catch (exception: Exception) {
            println("\n--------------------" +
                    "\nACTOR:CANTIDAD_PAGINAS_PROFESIONALES" +
                    "\nSTATUS:INTERNAL_SERVER_ERROR" +
                    "\n--------------------" +
                    "\nRESULTADO:${resultado}" +
                    "\n--------------------" +
                    "\n${exception.message}" +
                    "\n--------------------"
            )
            call.response.status(HttpStatusCode.InternalServerError)
        }
        call.respond(resultado)
    }

    put("$API_ROUTE/todos/{pagina}") {
        val resultado: MutableList<ProfesionalResponse> = ArrayList<ProfesionalResponse>()
        try {
            val pagina = call.parameters["pagina"]!!.toInt()
            val autenticacion = call.receive<Autenticacion>()
            if (jwtService.validateToken(autenticacion.token)) {
                if (autenticacion.tipo == "VECINO") {
                    val profesionales: List<Profesional> = profesionalService.get10Profesionales(pagina)
                    for (profesional in profesionales) {
                        resultado.add(profesionalToResponse(profesional))
                    }
                    call.response.status(HttpStatusCode.OK)
                    println("\n--------------------" +
                            "\nACTOR:10_PROFESIONALES" +
                            "\nSTATUS:OK" +
                            "\n--------------------" +
                            "\nUSUARIO:${autenticacion.tipo}" +
                            "\nTOKEN:${autenticacion.token}" +
                            "\nPAGINA:${pagina}" +
                            "\n--------------------" +
                            "\nRESULTADO:${resultado}" +
                            "\n--------------------"
                    )
                } else {
                    call.response.status(HttpStatusCode.Forbidden)
                    println("\n--------------------" +
                            "\nACTOR:10_PROFESIONALES" +
                            "\nSTATUS:FORBIDDEN" +
                            "\n--------------------" +
                            "\nUSUARIO:${autenticacion.tipo}" +
                            "\nTOKEN:${autenticacion.token}" +
                            "\nPAGINA:${pagina}" +
                            "\n--------------------" +
                            "\nRESULTADO:${resultado}" +
                            "\n--------------------"
                    )
                }
            } else {
                println("\n--------------------" +
                        "\nACTOR:10_PROFESIONALES" +
                        "\nSTATUS:UNAUTHORIZED" +
                        "\n--------------------" +
                        "\nUSUARIO:${autenticacion.tipo}" +
                        "\nTOKEN:${autenticacion.token}" +
                        "\nPAGINA:${pagina}" +
                        "\n--------------------" +
                        "\nRESULTADO:${resultado}" +
                        "\n--------------------"
                )
                call.response.status(HttpStatusCode.Unauthorized)
            }
        } catch (exposedSQLException: ExposedSQLException) {
            println("\n--------------------" +
                    "\nACTOR:10_PROFESIONALES" +
                    "\nSTATUS:INTERNAL_SERVER_ERROR" +
                    "\n--------------------" +
                    "\nRESULTADO:${resultado}" +
                    "\n--------------------" +
                    "\n${exposedSQLException.message}" +
                    "\n--------------------"
            )
            call.response.status(HttpStatusCode.BadRequest)
        } catch (nullPointerException: NullPointerException) {
            println("\n--------------------" +
                    "\nACTOR:10_PROFESIONALES" +
                    "\nSTATUS:INTERNAL_SERVER_ERROR" +
                    "\n--------------------" +
                    "\nRESULTADO:${resultado}" +
                    "\n--------------------" +
                    "\n${nullPointerException.message}" +
                    "\n--------------------"
            )
            call.response.status(HttpStatusCode.BadRequest)
        } catch (numberFormatException: NumberFormatException) {
            println("\n--------------------" +
                    "\nACTOR:10_PROFESIONALES" +
                    "\nSTATUS:INTERNAL_SERVER_ERROR" +
                    "\n--------------------" +
                    "\nRESULTADO:${resultado}" +
                    "\n--------------------" +
                    "\n${numberFormatException.message}" +
                    "\n--------------------"
            )
            call.response.status(HttpStatusCode.BadRequest)
        } catch (exception: Exception) {
            println("\n--------------------" +
                    "\nACTOR:10_PROFESIONALES" +
                    "\nSTATUS:INTERNAL_SERVER_ERROR" +
                    "\n--------------------" +
                    "\nRESULTADO:${resultado}" +
                    "\n--------------------" +
                    "\n${exception.message}" +
                    "\n--------------------"
            )
            call.response.status(HttpStatusCode.InternalServerError)
        }
        call.respond(resultado)
    }

    post("$API_ROUTE/nuevo") {
        val result: Profesional?
        try {
            val body = call.receive<AutenticacionProfesional>()
            val autenticacion = body.autenticacion
            val mapProfesional = body.profesional
            if (jwtService.validateToken(autenticacion.token)) {
                result = profesionalService.addProfesional(
                    mapProfesional,
                    cloudinaryConfig
                )
                call.response.status(HttpStatusCode.Created)
                println("\n--------------------" +
                        "\nSTATUS:OK" +
                        "\n--------------------"
                )
                if (result != null) {
                    call.respond(profesionalToResponse(result))
                }
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
                println("\n--------------------" +
                        "\nSTATUS:Unauthorized" +
                        "\n--------------------"
                )
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            println("\n--------------------" +
                    "\nSTATUS:BadRequest" +
                    "\n--------------------"
            )
            exposedSQLException.printStackTrace()
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println("\n--------------------" +
                    "\nSTATUS:InternalServerError" +
                    "\n--------------------"
            )
            exception.printStackTrace()
        }
    }

}

private fun requestToProfesional() {}

private fun profesionalToResponse(profesional: Profesional): ProfesionalResponse {
    return ProfesionalResponse(
        profesional.idProfesional,
        profesional.nombre,
        profesional.direccion,
        profesional.telefono,
        profesional.email,
        profesional.latitud,
        profesional.longitud,
        profesional.inicioJornada,
        profesional.finJornada,
        profesional.documento
    )
}