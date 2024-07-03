package ar.edu.uade.routes

import ar.edu.uade.mappers.responses.ComercioResponse
import ar.edu.uade.models.Comercio
import ar.edu.uade.services.ComercioService
import ar.edu.uade.services.JWTService
import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.CloudinaryConfig
import ar.edu.uade.utilities.containers.AutenticacionComercio
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Route.comercioRouting(jwtService: JWTService, comercioService: ComercioService, cloudinaryConfig: CloudinaryConfig) {
    val ruta ="/comercio"

    put("$ruta/todos/{pagina}") {
        val resultado: MutableList<ComercioResponse> = ArrayList<ComercioResponse>()
        try {
            val pagina = call.parameters["pagina"]!!.toInt()
            val autenticacion = call.receive<Autenticacion>()
            if (jwtService.validateToken(autenticacion.token)) {
                if (autenticacion.tipo == "VECINO") {
                    val comercios: List<Comercio> = comercioService.get10Comercios(pagina)
                    for (d in comercios) {
                        resultado.add(comercioToResponse(d))
                    }
                    call.response.status(HttpStatusCode.OK)
                    println(
                        "\n--------------------" +
                                "\nACTOR:10_COMERCIOS" +
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
                    println(
                        "\n--------------------" +
                                "\nACTOR:10_COMERCIOS" +
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
                println(
                    "\n--------------------" +
                            "\nACTOR:10_COMERCIOS" +
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
            println(
                "\n--------------------" +
                        "\nACTOR:10_COMERCIOS" +
                        "\nSTATUS:INTERNAL_SERVER_ERROR" +
                        "\n--------------------" +
                        "\nRESULTADO:${resultado}" +
                        "\n--------------------" +
                        "\n${exposedSQLException.message}" +
                        "\n--------------------"
            )
            call.response.status(HttpStatusCode.BadRequest)
        } catch (nullPointerException: NullPointerException) {
            println(
                "\n--------------------" +
                        "\nACTOR:10_COMERCIOS" +
                        "\nSTATUS:INTERNAL_SERVER_ERROR" +
                        "\n--------------------" +
                        "\nRESULTADO:${resultado}" +
                        "\n--------------------" +
                        "\n${nullPointerException.message}" +
                        "\n--------------------"
            )
            call.response.status(HttpStatusCode.BadRequest)
        } catch (numberFormatException: NumberFormatException) {
            println(
                "\n--------------------" +
                        "\nACTOR:10_COMERCIOS" +
                        "\nSTATUS:INTERNAL_SERVER_ERROR" +
                        "\n--------------------" +
                        "\nRESULTADO:${resultado}" +
                        "\n--------------------" +
                        "\n${numberFormatException.message}" +
                        "\n--------------------"
            )
            call.response.status(HttpStatusCode.BadRequest)
        } catch (exception: Exception) {
            println(
                "\n--------------------" +
                        "\nACTOR:10_COMERCIOS" +
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

    put("$ruta/particular/{id}") {
        var resultado: ComercioResponse? = null
        try {
            val id = call.parameters["id"]!!.toInt()
            val autenticacion = call.receive<Autenticacion>()
            if (jwtService.validateToken(autenticacion.token)) {
                val comercio = comercioService.getComercioById(id)
                if (comercio != null) {
                    resultado = comercioToResponse(comercio)
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
        if (resultado != null) {
            call.respond(resultado)
        }
    }

    put("$ruta/cantidadPaginas") {
        var resultado: Int = 0
        try {
            val autenticacion = call.receive<Autenticacion>()
            if (jwtService.validateToken(autenticacion.token)) {
                if (autenticacion.tipo == "VECINO") {
                    resultado = comercioService.getCantidadPaginas()
                    println("\n--------------------" +
                            "\nACTOR:CANTIDAD_PAGINAS_COMERCIOS" +
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
                            "\nACTOR:CANTIDAD_PAGINAS_COMERCIOS" +
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
                        "\nACTOR:CANTIDAD_PAGINAS_COMERCIOS" +
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
                    "\nACTOR:CANTIDAD_PAGINAS_COMERCIOS" +
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

    get("$ruta/fotos/{idComercio}") {
        var resultado: List<String> = ArrayList()
        try {
            val id = call.parameters["idComercio"]?.toIntOrNull()
            if (id != null) {
                resultado = comercioService.getFotos(id)
                call.response.status(HttpStatusCode.OK)
                call.respond(resultado)
                println(
                    "\n--------------------" +
                            "\nACTOR:FOTOS COMERCIO" +
                            "\nSTATUS:OK" +
                            "\n--------------------"
                )
            } else {
                call.response.status(HttpStatusCode.BadRequest)
                println(
                    "\n--------------------" +
                            "\nACTOR:FOTOS COMERCIO" +
                            "\nSTATUS:BAD_REQUEST" +
                            "\n--------------------" +
                            "\nEL ID DEL COMERCIO ES:${id}" +
                            "\n--------------------"
                )
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            println(
                "\n--------------------" +
                        "\nACTOR:FOTOS COMERCIO" +
                        "\nSTATUS:BAD_REQUEST" +
                        "\n--------------------" +
                        "\n${exposedSQLException.message}" +
                        "\n--------------------"

            )
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nACTOR:FOTOS COMERCIO" +
                        "\nSTATUS:INTERNAL_SERVER_ERROR" +
                        "\n--------------------" +
                        "\n${exception.message}" +
                        "\n--------------------"
            )
        }
        call.respond(resultado)
    }

    post("$ruta/nuevo") {
        val result: Comercio?
        try {
            val body = call.receive<AutenticacionComercio>()
            val autenticacion = body.autenticacion
            val mapComercio = body.comercio
            if (jwtService.validateToken(autenticacion.token)) {
                result = comercioService.addComercio(mapComercio,cloudinaryConfig)
                call.response.status(HttpStatusCode.Created)
                println("\n--------------------" +
                            "\nSTATUS:COMERCIO CREATED" +
                        "\n--------------------"
                )
                if (result != null) {
                    call.respond(comercioToResponse(result))
                }
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
                println("\n--------------------" +
                        "\nSTATUS:COMERCIO UNAUTHORIZED" +
                        "\n--------------------"
                )
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            println("\n--------------------" +
                    "\nSTATUS:COMERCIO BAD REQUEST" +
                    "\n--------------------"
            )
            exposedSQLException.printStackTrace()
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println("\n--------------------" +
                    "\nSTATUS:COMERCIO INTERNAL SERVER ERROR" +
                    "\n--------------------"
            )
            exception.printStackTrace()
        }
    }

    put("$ruta/propios/{documentoVecino}") {
        var resultado: List<ComercioResponse> = ArrayList()
        try {
            val documentoVecino = call.parameters["documentoVecino"]!!
            val autenticacion = call.receive<Autenticacion>()
            println("AAAAAAAAAAAAAAAAAAAAAAAAAAA")
            print("Documento Vecino: " + documentoVecino)
            if (jwtService.validateToken(autenticacion.token)) {
                resultado = comercioService.getComerciosByVecino(documentoVecino).map { comercioToResponse(it) }
                call.response.status(HttpStatusCode.OK)
                println(
                    "\n--------------------" +
                            "\nACTOR:COMERCIOS_VECINO" +
                            "\nSTATUS:OK" +
                            "\n--------------------" +
                            "\nUSUARIO:${autenticacion.tipo}" +
                            "\nTOKEN:${autenticacion.token}" +
                            "\nDOCUMENTO_VECINO:${documentoVecino}" +
                            "\n--------------------" +
                            "\nRESULTADO:${resultado}" +
                            "\n--------------------"
                )
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
                println(
                    "\n--------------------" +
                            "\nACTOR:COMERCIOS_VECINO" +
                            "\nSTATUS:UNAUTHORIZED" +
                            "\n--------------------" +
                            "\nUSUARIO:${autenticacion.tipo}" +
                            "\nTOKEN:${autenticacion.token}" +
                            "\nDOCUMENTO_VECINO:${documentoVecino}" +
                            "\n--------------------"
                )
            }
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nACTOR:COMERCIOS_VECINO" +
                        "\nSTATUS:INTERNAL_SERVER_ERROR" +
                        "\n--------------------" +
                        "\nERROR:${exception.message}" +
                        "\n--------------------"
            )
        }
        call.respond(resultado)
    }

    get(ruta+"/habilitar"+"/{idComercio}") {
        try {
            val idComercio = call.parameters["idComercio"]!!.toInt()
            if (idComercio != null) {
                if (comercioService.habilitarComercio(idComercio)) {
                    call.response.status(HttpStatusCode.OK)
                    println(
                        "\n--------------------" +
                                "\nACTOR:HABILITACION COMERCIO" +
                                "\nSTATUS:OK" +
                                "\n--------------------" +
                                "\nDATOS:" +
                                "\n--------------------"
                    )
                }
                else {
                    call.response.status(HttpStatusCode.BadRequest)
                    println(
                        "\n--------------------" +
                                "\nACTOR:HABILITACION_COMERCIO" +
                                "\nSTATUS:BAD_REQUEST" +
                                "\n--------------------" +
                                "\nDATOS:" +
                                "\n--------------------"
                    )
                }
            }
            else {
                call.response.status(HttpStatusCode.BadRequest)
                println(
                    "\n--------------------" +
                            "\nACTOR:HABILITACION_COMERCIO" +
                            "\nSTATUS:BAD_REQUEST" +
                            "\n--------------------" +
                            "\nDATOS:" +
                            "\n--------------------"
                )
            }
        }
        catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nACTOR:HABILITACION_COMERCIO" +
                        "\nSTATUS:INTERNAL_SERVER_ERROR" +
                        "\n--------------------" +
                        "\nERROR:${exception.message}" +
                        "\n--------------------"
            )
        }
        call.respond(true)
    }
}



private fun comercioToResponse(d: Comercio): ComercioResponse{
    return ComercioResponse(
        d.idComercio,
        d.nombre,
        d.documento,
        d.direccion,
        d.descripcion,
        d.telefono,
        d.apertura,
        d.cierre,
        d.latitud,
        d.longitud
    )
}


