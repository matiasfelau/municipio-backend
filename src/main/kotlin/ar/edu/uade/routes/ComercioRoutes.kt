package ar.edu.uade.routes

import ar.edu.uade.mappers.requests.ComercioRequest
import ar.edu.uade.mappers.requests.DenunciaRequest
import ar.edu.uade.mappers.responses.ComercioResponse
import ar.edu.uade.mappers.responses.DenunciaResponse
import ar.edu.uade.models.Comercio
import ar.edu.uade.models.Denuncia
import ar.edu.uade.models.Reclamo
import ar.edu.uade.services.ComercioService
import ar.edu.uade.services.JWTService
import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.CloudinaryConfig
import ar.edu.uade.utilities.containers.AutenticacionComercio
import ar.edu.uade.utilities.containers.AutenticacionReclamo
import com.cloudinary.utils.ObjectUtils
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.io.File

fun Route.comercioRouting(jwtService: JWTService, comercioService: ComercioService, cloudinaryConfig: CloudinaryConfig) {
    val ruta ="/comercio"

    put("$ruta/todos/{pagina}") {
        val resultado: MutableList<ComercioResponse> = ArrayList<ComercioResponse>()
        try {
            val pagina = call.parameters["pagina"]!!.toInt()
            val auth = call.receive<Autenticacion>()
            if (jwtService.validateToken(auth.token)) {
                val comercios = comercioService.getComercios(pagina)
                for (d in comercios) {
                    resultado.add(comercioToResponse(d))
                }
                call.response.status(HttpStatusCode.OK)
                println(
                    "\n--------------------" +
                            "\nSTATUS:COMERCIO OK" +
                            "\n--------------------" +
                            "\nUSUARIO:${auth.tipo}" +
                            "\nPAGINA:${pagina}" +
                            "\n--------------------" +
                            "\nRESULTADO:${resultado}" +
                            "\n--------------------"
                )
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
                println(
                    "\n--------------------" +
                            "\nSTATUS:COMERCIO UNAUTHORIZED" +
                            "\n--------------------" +
                            "\nUSUARIO:${auth.tipo}" +
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

    post("$ruta/subirImagenes/{idComercio}") {
        val idComercio = call.parameters["idComercio"]?.toIntOrNull()
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
                    if (idComercio != null) {
                        comercioService.addImageToComercio(idComercio, urlImagen!!)
                    }
                }
                file.delete()
            }
            part.dispose()
        }
    }

    put("$ruta/cantidadPaginas") {
        try {
            val body = call.receive<Autenticacion>()
            if (jwtService.validateToken(body.token)) {
                val cantPaginas = comercioService.getCantidadPaginas()
                call.response.status(HttpStatusCode.OK)
                call.respond(cantPaginas)
                println(
                    "\n--------------------" +
                            "\nACTOR:PAGINAS COMERCIO" +
                            "\nSTATUS:OK" +
                            "\n--------------------" +
                            "\nUSUARIO:${body.tipo}" +
                            "\nCANTIDAD DE PAGINAS:${cantPaginas}" +
                            "\n--------------------"
                )
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
                println(
                    "\n--------------------" +
                            "\nACTOR:PAGINAS COMERCIO" +
                            "\nSTATUS:UNAUTHORIZED" +
                            "\n--------------------" +
                            "\nUSUARIO:${body.tipo}" +
                            "\n--------------------"
                )
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            println(
                "\n--------------------" +
                        "\nACTOR:PAGINAS COMERCIO" +
                        "\nSTATUS:BAD REQUEST" +
                        "\n--------------------" +
                        "\n${exposedSQLException.message}" +
                        "\n--------------------"
            )
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nACTOR:PAGINAS COMERCIO" +
                        "\nSTATUS:INTERNAL SERVER ERROR" +
                        "\n--------------------" +
                        "\n${exception.message}" +
                        "\n--------------------"
            )
        }
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
            val request = call.receive<AutenticacionComercio>()
            val autenticacion = request.autenticacion
            val comercioRQ = request.comercio
            if (jwtService.validateToken(autenticacion.token)) {
                result = comercioService.addComercio(RequestToComercio(comercioRQ))
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
}

private fun comercioToResponse(d: Comercio): ComercioResponse{
    return ComercioResponse(
        d.idComercio,
        d.nombre,
        d.apertura,
        d.cierre,
        d.direccion,
        d.telefono,
        d.descripcion,
        d.latitud,
        d.longitud,
        d.documento
    )
}

private fun RequestToComercio(d: ComercioRequest): Comercio {
    return Comercio(
        d.idComercio,
        d.nombre,
        d.apertura,
        d.cierre,
        d.direccion,
        d.telefono,
        d.descripcion,
        d.latitud,
        d.longitud,
        d.documento
    )
}