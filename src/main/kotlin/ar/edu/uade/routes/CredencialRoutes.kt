package ar.edu.uade.routes

import ar.edu.uade.mappers.requests.CredencialRequest
import ar.edu.uade.mappers.responses.CredencialResponse
import ar.edu.uade.models.Credencial
import ar.edu.uade.services.CredencialJWTService
import ar.edu.uade.services.CredencialService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.credencialRouting(credencialService: CredencialService, credencialJwtService: CredencialJWTService) {

    post("/vecino/registro") {
        try {
            val request = call.receive<CredencialRequest>()
            val credencial = credencialService.solicitarCredencial(request.documento, request.email)
            if (credencial != null) {
                call.response.status(HttpStatusCode.Created)
                println("\n--------------------" +
                        "\nACTOR:REGISTRO CREDENCIAL VECINO" +
                        "\nSTATUS:CREATED" +
                        "\n--------------------" +
                        "\nDATOS:${credencial.documento},${credencial.email}" +
                        "\n--------------------"
                )
            }
            else {
                call.response.status(HttpStatusCode.BadRequest)
                println("\n--------------------" +
                        "\nACTOR:REGISTRO CREDENCIAL VECINO" +
                        "\nSTATUS:BAD REQUEST" +
                        "\n--------------------" +
                        "\nDATOS:${request.documento},${request.email}" +
                        "\n--------------------"
                )
            }
        }
        catch (nullPointerException: NullPointerException) {
            call.response.status(HttpStatusCode.BadRequest)
            println("\n--------------------" +
                    "\nACTOR:REGISTRO CREDENCIAL VECINO" +
                    "\nSTATUS:BAD REQUEST" +
                    "\n--------------------" +
                    "\nERROR:${nullPointerException.message}" +
                    "\n--------------------"
            )
        }
        catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println("\n--------------------" +
                    "\nACTOR:REGISTRO CREDENCIAL VECINO" +
                    "\nSTATUS:INTERNAL SERVER ERROR" +
                    "\n--------------------" +
                    "\nERROR:${exception.message}" +
                    "\n--------------------"
            )
        }
        call.respond(true)
    }

    get("/vecino/habilitacion/{documento}") {
        try {
            val documento = call.parameters["documento"]
            if (documento != null) {
                val credencial = credencialService.find(documento)
                if (credencial != null) {
                    if (credencialService.habilitarCredencial(credencial)) {
                        call.response.status(HttpStatusCode.OK)
                        println(
                            "\n--------------------" +
                                    "\nACTOR:HABILITACION CREDENCIAL VECINO" +
                                    "\nSTATUS:OK" +
                                    "\n--------------------" +
                                    "\nDATOS:${credencial.documento}" +
                                    "\n--------------------"
                        )
                    }
                    else {
                        call.response.status(HttpStatusCode.BadRequest)
                        println(
                            "\n--------------------" +
                                    "\nACTOR:HABILITACION_CREDENCIAL_VECINO" +
                                    "\nSTATUS:BAD_REQUEST" +
                                    "\n--------------------" +
                                    "\nDATOS:${credencial.documento}" +
                                    "\n--------------------"
                        )
                    }
                }
                else {
                    call.response.status(HttpStatusCode.NotFound)
                    println(
                        "\n--------------------" +
                                "\nACTOR:HABILITACION_CREDENCIAL_VECINO" +
                                "\nSTATUS:NOT_FOUND" +
                                "\n--------------------" +
                                "\nDATOS:${documento}" +
                                "\n--------------------"
                    )
                }
            }
            else {
                call.response.status(HttpStatusCode.BadRequest)
                println(
                    "\n--------------------" +
                            "\nACTOR:HABILITACION_CREDENCIAL_VECINO" +
                            "\nSTATUS:BAD_REQUEST" +
                            "\n--------------------" +
                            "\nDATOS:${documento}" +
                            "\n--------------------"
                )
            }
        }
        catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nACTOR:HABILITACION_CREDENCIAL_VECINO" +
                        "\nSTATUS:INTERNAL_SERVER_ERROR" +
                        "\n--------------------" +
                        "\nERROR:${exception.message}" +
                        "\n--------------------"
            )
        }
        call.respond(true)
    }

    put("/vecino/primer-ingreso") {
        try {
            val request = call.receive<CredencialRequest>()
            if (credencialService.casoPrimerIngresoCredencial(request)) {
                call.response.status(HttpStatusCode.OK)
                println(
                    "\n--------------------" +
                            "\nACTOR:PRIMER_INGRESO_MODIFICACION_CREDENCIAL_VECINO" +
                            "\nSTATUS:OK" +
                            "\n--------------------" +
                            "\nDATOS:${request.documento}" +
                            "\n--------------------"
                )
            }
            else {
                call.response.status(HttpStatusCode.BadRequest)
                println(
                    "\n--------------------" +
                            "\nACTOR:PRIMER_INGRESO_MODIFICACION_CREDENCIAL_VECINO" +
                            "\nSTATUS:BAD_REQUEST" +
                            "\n--------------------" +
                            "\nDATOS:${request.documento}" +
                            "\n--------------------"
                )
            }
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nACTOR:PRIMER_INGRESO_MODIFICACION_CREDENCIAL_VECINO" +
                        "\nSTATUS:INTERNAL_SERVER_ERROR" +
                        "\n--------------------" +
                        "\nERROR:${exception.message}" +
                        "\n--------------------"
            )
        }
        call.respond(true)
    }

    post("/vecino/ingreso") {
        try {
            val request = call.receive<CredencialRequest>()
            val token: String? = credencialJwtService.createJwtToken(request)
            token?.let {
                call.response.status(HttpStatusCode.OK)
                call.respond(hashMapOf("token" to token))
            } ?: call.respond(message = HttpStatusCode.Unauthorized)
        } catch (e: Exception) {
            call.respond(message = HttpStatusCode.InternalServerError)
        }
    }

    get("/vecino/confirmar-primer-ingreso/{documento}") {
        try {
            val documento = call.parameters["documento"]
            if (documento != null) {
                val credencial = credencialService.find(documento)
                if (credencial != null) {
                    if (credencial.primerIngreso) {
                        call.response.status(HttpStatusCode.OK)
                        println(
                            "\n--------------------" +
                                    "\nACTOR:CONFIRMACION_PRIMER_INGRESO_CREDENCIAL_VECINO" +
                                    "\nSTATUS:OK" +
                                    "\n--------------------" +
                                    "\nDATOS:${credencial.documento}" +
                                    "\n--------------------"
                        )
                    }
                    else {
                        call.response.status(HttpStatusCode.BadRequest)
                        println(
                            "\n--------------------" +
                                    "\nACTOR:CONFIRMACION_PRIMER_INGRESO_CREDENCIAL_VECINO" +
                                    "\nSTATUS:BAD_REQUEST" +
                                    "\n--------------------" +
                                    "\nDATOS:${credencial.documento}" +
                                    "\n--------------------"
                        )
                    }
                }
                else {
                    call.response.status(HttpStatusCode.NotFound)
                    println(
                        "\n--------------------" +
                                "\nACTOR:CONFIRMACION_PRIMER_INGRESO_CREDENCIAL_VECINO" +
                                "\nSTATUS:NOT_FOUND" +
                                "\n--------------------" +
                                "\nDATOS:${documento}" +
                                "\n--------------------"
                    )
                }
            }
            else {
                call.response.status(HttpStatusCode.BadRequest)
                println(
                    "\n--------------------" +
                            "\nACTOR:CONFIRMACION_PRIMER_INGRESO_CREDENCIAL_VECINO" +
                            "\nSTATUS:BAD_REQUEST" +
                            "\n--------------------" +
                            "\nDATOS:${documento}" +
                            "\n--------------------"
                )
            }
        }
        catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nACTOR:CONFIRMACION_PRIMER_INGRESO_CREDENCIAL_VECINO" +
                        "\nSTATUS:INTERNAL_SERVER_ERROR" +
                        "\n--------------------" +
                        "\nERROR:${exception.message}" +
                        "\n--------------------"
            )
        }
        call.respond(true)
    }

    put("/vecino/recuperacion") {
        try {
            val request = call.receive<CredencialRequest>()
            val credencial = credencialService.find(request.documento)
            if (credencial != null) {
                if (request.email == credencial.email) {
                    if (credencialService.recuperarCredencial(credencial)) {
                        call.response.status(HttpStatusCode.OK)
                        println(
                            "\n--------------------" +
                                    "\nACTOR:RECUPERACION_CREDENCIAL_VECINO" +
                                    "\nSTATUS:OK" +
                                    "\n--------------------" +
                                    "\nDATOS:${credencial.documento},${credencial.email}" +
                                    "\n--------------------"
                        )
                    }
                    else {
                        call.response.status(HttpStatusCode.BadRequest)
                        println(
                            "\n--------------------" +
                                    "\nACTOR:RECUPERACION_CREDENCIAL_VECINO" +
                                    "\nSTATUS:BAD_REQUEST" +
                                    "\n--------------------" +
                                    "\nDATOS:${credencial.documento},${credencial.email}" +
                                    "\n--------------------"
                        )
                    }
                }
                else {
                    call.response.status(HttpStatusCode.BadRequest)
                    println(
                        "\n--------------------" +
                                "\nACTOR:RECUPERACION_CREDENCIAL_VECINO" +
                                "\nSTATUS:BAD_REQUEST" +
                                "\n--------------------" +
                                "\nDATOS:${credencial.documento},${credencial.email}" +
                                "\n--------------------"
                    )
                }
            }
            else {
                call.response.status(HttpStatusCode.NotFound)
                println(
                    "\n--------------------" +
                            "\nACTOR:RECUPERACION_CREDENCIAL_VECINO" +
                            "\nSTATUS:NOT_FOUND" +
                            "\n--------------------" +
                            "\nDATOS:${request.documento}" +
                            "\n--------------------"
                )
            }
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nACTOR:RECUPERACION_CREDENCIAL_VECINO" +
                        "\nSTATUS:INTERNAL_SERVER_ERROR" +
                        "\n--------------------" +
                        "\nERROR:${exception.message}" +
                        "\n--------------------"
            )
        }
        call.respond(true)
    }
}

fun credencialToResponse(credencial: Credencial): CredencialResponse {
    return CredencialResponse(
        credencial.documento,
        credencial.email,
        credencial.primerIngreso
    )
}
