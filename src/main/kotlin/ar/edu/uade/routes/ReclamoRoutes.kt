package ar.edu.uade.routes

import ar.edu.uade.mappers.requests.ReclamoRequest
import ar.edu.uade.mappers.responses.ReclamoResponse
import ar.edu.uade.models.Reclamo
import ar.edu.uade.services.JWTService
import ar.edu.uade.services.ReclamoService
import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.CloudinaryConfig
import ar.edu.uade.utilities.containers.AutenticacionFiltro
import ar.edu.uade.utilities.containers.AutenticacionReclamo
import com.cloudinary.utils.ObjectUtils
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.io.File


fun Route.reclamoRouting(jwtService: JWTService, reclamoService: ReclamoService, cloudinaryConfig: CloudinaryConfig) {
    val ruta = "/reclamo"

    put("$ruta/todos/{pagina}") {
        val resultado: MutableList<ReclamoResponse> = ArrayList<ReclamoResponse>()
        try {
            val pagina = call.parameters["pagina"]!!.toInt()
            val body = call.receive<AutenticacionFiltro>()
            val autenticacion = body.autenticacion
            val filtro = body.filtro
            if (jwtService.validateToken(autenticacion.token)) {
                if ((autenticacion.tipo == "Vecino") || (autenticacion.tipo == "Empleado" && filtro.tipo == "sector")) {
                    val reclamos = reclamoService.getReclamosByFiltro(pagina, filtro)
                    for (reclamo in reclamos) {
                        resultado.add(reclamoToResponse(reclamo))
                    }
                    call.response.status(HttpStatusCode.OK)
                    println(
                        "\n--------------------" +
                                "\nSTATUS:OK" +
                                "\n--------------------" +
                                "\nUSUARIO:${autenticacion.tipo}" +
                                "\nFILTRO:${filtro.tipo},${filtro.dato}" +
                                "\nPAGINA:${pagina}" +
                                "\n--------------------"
                    )
                } else {
                    call.response.status(HttpStatusCode.Forbidden)
                    println(
                        "\n--------------------" +
                                "\nSTATUS:FORBIDDEN" +
                                "\n--------------------" +
                                "\nUSUARIO:${autenticacion.tipo}" +
                                "\nFILTRO:${filtro.tipo},${filtro.dato}" +
                                "\nPAGINA:${pagina}" +
                                "\n--------------------"
                    )
                }
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
                println(
                    "\n--------------------" +
                            "\nSTATUS:UNAUTHORIZED" +
                            "\n--------------------" +
                            "\nUSUARIO:${autenticacion.tipo}" +
                            "\nFILTRO:${filtro.tipo},${filtro.dato}" +
                            "\nPAGINA:${pagina}" +
                            "\n--------------------"
                )
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            exposedSQLException.printStackTrace()
        } catch (nullPointerException: NullPointerException) {
            call.response.status(HttpStatusCode.BadRequest)
            nullPointerException.printStackTrace()
        } catch (numberFormatException: NumberFormatException) {
            call.response.status(HttpStatusCode.BadRequest)
            numberFormatException.printStackTrace()
        } catch (noSuchMethodException: NoSuchMethodException) {
            call.response.status(HttpStatusCode.BadRequest)
            noSuchMethodException.printStackTrace()
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            exception.printStackTrace()
        }
        call.respond(resultado)
    }

    put("$ruta/particular/{id}") {
        var resultado: ReclamoResponse = ReclamoResponse(null, null, null, null, null)
        try {
            val id = call.parameters["id"]!!.toInt()
            val autenticacion = call.receive<Autenticacion>()
            if (jwtService.validateToken(autenticacion.token)) {
                val reclamo = reclamoService.getReclamoById(id)
                if (reclamo != null) {
                    resultado = reclamoToResponse(reclamo)
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
        call.respond(resultado)
    }

    post("$ruta/nuevo") {
        val result: Reclamo?
        try {
            val request = call.receive<AutenticacionReclamo>()
            val autenticacion = request.autenticacion
            val reclamoRQ = request.reclamo
            if (jwtService.validateToken(autenticacion.token)) {
                result = reclamoService.createReclamo(requestToReclamo(reclamoRQ))
                call.response.status(HttpStatusCode.Created)
                println(
                    "\n--------------------" +
                            "\nSTATUS:RECLAMO CREATED" +
                            "\n--------------------"
                )
                if (result != null) {
                    call.respond(reclamoToResponse(result))
                }
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
                println(
                    "\n--------------------" +
                            "\nSTATUS:RECLAMO UNAUTHORIZED" +
                            "\n--------------------"
                )
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            println(
                "\n--------------------" +
                        "\nSTATUS:RECLAMO BAD REQUEST" +
                        "\n--------------------"
            )
            exposedSQLException.printStackTrace()
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nSTATUS:RECLAMO INTERNAL SERVER ERROR" +
                        "\n--------------------"
            )
            exception.printStackTrace()
        }
    }

    post("$ruta/subirImagenes/{idReclamo}") {

        println("entra")

        val idReclamo = call.parameters["idReclamo"]?.toIntOrNull()


        val multipart = call.receiveMultipart()

        val uploadDir = File("D:\\Prueba")
        if (uploadDir.mkdir()) {

            // display that the directory is created
            // as the function returned true
            System.out.println("Directory is created");
        } else {
            // display that the directory cannot be created
            // as the function returned false
            System.out.println("Directory cannot be created");
        }

        var urlImagen: String? = null

        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                val file = File("D:\\Prueba/${part.originalFileName}")
                part.streamProvider().use { input ->
                    file.outputStream().buffered().use { output ->
                        input.copyTo(output)
                    }
                }
                val uploadResult = cloudinaryConfig.cloudinary.uploader().upload(file, ObjectUtils.emptyMap())
                urlImagen = uploadResult["url"] as String
                if (urlImagen != null) {
                    if (idReclamo != null) {
                        reclamoService.addImageToReclamo(idReclamo, urlImagen!!)
                    }
                }
                file.delete()
            }
            part.dispose()
        }
    }

    put("$ruta/cantidadPaginas") {
        try {
            val body = call.receive<AutenticacionFiltro>()
            if (jwtService.validateToken(body.autenticacion.token)) {
                if ((body.autenticacion.tipo == "Vecino") || (body.autenticacion.tipo == "Empleado" && body.filtro.tipo == "sector")) {
                    val cantPaginas = reclamoService.getCantidadPaginas(body.filtro)
                    call.response.status(HttpStatusCode.OK)
                    call.respond(cantPaginas)

                    println(
                        "\n--------------------" +
                                "\nSTATUS:OK" +
                                "\n--------------------" +
                                "\nUSUARIO:${body.autenticacion.tipo}" +
                                "\nFILTRO:${body.filtro.tipo},${body.filtro.dato}" +
                                "\nCANTIDAD DE PAGINAS:${cantPaginas}" +
                                "\n--------------------"
                    )
                } else {
                    call.response.status(HttpStatusCode.Forbidden)
                    println(
                        "\n--------------------" +
                                "\nSTATUS:FORBIDDEN" +
                                "\n--------------------" +
                                "\nUSUARIO:${body.autenticacion.tipo}" +
                                "\nFILTRO:${body.filtro.tipo},${body.filtro.dato}" +
                                "\n--------------------"
                    )
                }

            } else {
                call.response.status(HttpStatusCode.Unauthorized)
                println(
                    "\n--------------------" +
                            "\nSTATUS:UNAUTHORIZED" +
                            "\n--------------------" +
                            "\nUSUARIO:${body.autenticacion.tipo}" +
                            "\nFILTRO:${body.filtro.tipo},${body.filtro.dato}" +
                            "\n--------------------"
                )
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            println(
                "\n--------------------" +
                        "\nSTATUS:BAD REQUEST" +
                        "\n--------------------"
            )
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nSTATUS:INTERNAL SERVER ERROR" +
                        "\n--------------------"
            )
            println(exception.message)
            exception.printStackTrace()
        }
    }

    get("$ruta/fotos/{idReclamo}") {
        var resultado: List<String> = ArrayList()
        try {
            val id = call.parameters["idReclamo"]?.toIntOrNull()
            if (id != null) {
                resultado = reclamoService.getFotos(id)
                call.response.status(HttpStatusCode.OK)
                println("\n--------------------" +
                        "\nACTOR:FOTOS" +
                        "\nSTATUS:OK" +
                        "\n--------------------"
                )
            } else {
                call.response.status(HttpStatusCode.BadRequest)
                println("\n--------------------" +
                        "\nACTOR:FOTOS" +
                        "\nSTATUS:BAD_REQUEST" +
                        "\n--------------------" +
                        "\nEL ID DEL RECLAMO ES:${id}" +
                        "\n--------------------"
                )
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            println("\n--------------------" +
                    "\nACTOR:FOTOS" +
                    "\nSTATUS:BAD_REQUEST" +
                    "\n--------------------" +
                    "\n${exposedSQLException.message}" +
                    "\n--------------------"

            )
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println("\n--------------------" +
                    "\nACTOR:FOTOS" +
                    "\nSTATUS:INTERNAL_SERVER_ERROR" +
                    "\n--------------------" +
                    "\n${exception.message}" +
                    "\n--------------------"
            )
        }
        call.respond(resultado)
    }
}

private fun requestToReclamo(reclamoRQ: ReclamoRequest): Reclamo {
    return Reclamo(
        null,
        reclamoRQ.descripcion,
        reclamoRQ.estado,
        reclamoRQ.documento,
        reclamoRQ.idSitio,
        reclamoRQ.idDesperfecto,
        null
    )
}

private fun reclamoToResponse(reclamo: Reclamo): ReclamoResponse {
    return ReclamoResponse(
        reclamo.idReclamo,
        reclamo.descripcion,
        reclamo.estado,
        reclamo.documento,
        reclamo.idSitio
    )
}