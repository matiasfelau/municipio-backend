package ar.edu.uade.routes

import ar.edu.uade.mappers.requests.ComercioDenunciadoRequest
import ar.edu.uade.mappers.requests.DenunciaRequest
import ar.edu.uade.mappers.requests.VecinoDenunciadoRequest
import ar.edu.uade.mappers.responses.DenunciaResponse
import ar.edu.uade.mappers.responses.DenunciadoResponse
import ar.edu.uade.models.ComercioDenunciado
import ar.edu.uade.models.Denuncia
import ar.edu.uade.models.VecinoDenunciado
import ar.edu.uade.services.DenunciaService
import ar.edu.uade.services.JWTService
import ar.edu.uade.services.VecinoService
import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.CloudinaryConfig
import ar.edu.uade.utilities.containers.AutenticacionDenuncia
import ar.edu.uade.utilities.containers.AutenticacionDenunciaComercio
import ar.edu.uade.utilities.containers.AutenticacionDenunciaVecino
import com.cloudinary.utils.ObjectUtils
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.io.File

fun Route.denunciaRouting(jwtService: JWTService, denunciaService: DenunciaService, vecinoService: VecinoService, /* movimientoDenunciaService: MovimientoDenunciaService ,*/ cloudinaryConfig: CloudinaryConfig) {
    val ruta = "/denuncia"

    put("$ruta/todos/{pagina}") {
        //TODO EDITAR PARA QUE SOLO PUEDAS VER TUS DENUNCIAS Y A LAS CUALES TE FUERON DENUNCIADAS

        val resultado: MutableList<DenunciaResponse> = ArrayList<DenunciaResponse>()
        try {
            val pagina = call.parameters["pagina"]!!.toInt()
            val body = call.receive<AutenticacionDenuncia>()
            val auth = body.autenticacion
            val documento = body.documento
            if (jwtService.validateToken(auth.token)) {
                val denuncias = denunciaService.getDenuncias(pagina,documento)
                for (d in denuncias) {
                    resultado.add(denunciaToResponse(d))
                }
                call.response.status(HttpStatusCode.OK)
                println(
                    "\n--------------------" +
                            "\nSTATUS:DENUNCIA OK" +
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
                            "\nSTATUS:DENUNCIA UNAUTHORIZED" +
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
        var resultado: DenunciaResponse? = null
        try {
            val id = call.parameters["id"]!!.toInt()
            val autenticacion = call.receive<Autenticacion>()
            if (jwtService.validateToken(autenticacion.token)) {
                val denuncia = denunciaService.getDenunciaById(id)
                if (denuncia != null) {
                    resultado = denunciaToResponse(denuncia)
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

    get("$ruta/denunciado/{id}"){
        var resultado: DenunciadoResponse = DenunciadoResponse(null)
        try{
            val id = call.parameters["id"]!!.toInt()
            val denunciado = denunciaService.getDenunciado(id);
            if (denunciado != null && denunciado != "") {
                val denunciadoResponse = DenunciadoResponse(
                    denunciado
                )
                resultado = denunciadoResponse
                call.response.status(HttpStatusCode.OK)
                println("DENUNCIADO ENTREGADO \n"
                + denunciado)
            } else {
                call.response.status(HttpStatusCode.NotFound)
                println("DENUNCIADO NO ENCONTRADO")
            }
        } catch (nullPointerException: NullPointerException) {
        call.response.status(HttpStatusCode.BadRequest)
            println("DENUNCIADO BAD REQUEST")
        } catch (numberFormatException: NumberFormatException) {
        call.response.status(HttpStatusCode.BadRequest)
            println("DENUNCIADO BAD REQUEST")
        } catch (exception: Exception) {
        call.response.status(HttpStatusCode.InternalServerError)
            println("DENUNCIADO INTERNAL SERVER ERROR")
        }
        call.respond(resultado)
    }

    post("$ruta/nueva-denuncia-comercio") {
        val result: Denuncia?
        try {
            val request = call.receive<AutenticacionDenunciaComercio>()
            val autenticacion = request.autenticacion
            val denuncia = request.containerDenunciaComercio
            if (jwtService.validateToken(autenticacion.token)) {
                result = denunciaService.addDenunciaComercio(
                    requestToDenuncia(denuncia.denuncia),
                    //TODO USAR COMERCIOSERVICE
                    serializableToComercioDenunciado(denuncia.comercioDenunciado)
                )
                call.response.status(HttpStatusCode.Created)
                println(
                    "\n--------------------" +
                            "\nSTATUS:DENUNCIA CREATED" +
                            "\n--------------------"
                )
                if (result != null) {
                    call.respond(denunciaToResponse(result))
                }
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
                println(
                    "\n--------------------" +
                            "\nSTATUS:DENUNCIA UNAUTHORIZED" +
                            "\n--------------------"
                )
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            println(
                "\n--------------------" +
                        "\nSTATUS:DENUNCIA BAD REQUEST" +
                        "\n--------------------"
            )
            exposedSQLException.printStackTrace()
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nSTATUS:DENUNCIA INTERNAL SERVER ERROR" +
                        "\n--------------------"
            )
            exception.printStackTrace()
        }
    }

    post("$ruta/nueva-denuncia-vecino") {
        //TODO IMPLEMENTAR BUSQUEDA DE USUARIO A QUIEN SE HA DENUNCIADO
        val result: Denuncia?
        try {
            val request = call.receive<AutenticacionDenunciaVecino>()
            val autenticacion = request.autenticacion
            val denuncia = request.denuncia
            val vecinoDenunciado = requestToVecinoDenunciado(denuncia.vecinoDenunciado)
            vecinoDenunciado.documento = vecinoService.getVecinoSegunNomApDir(vecinoDenunciado.nombre, vecinoDenunciado.apellido, vecinoDenunciado.direccion)?.documento
            if (jwtService.validateToken(autenticacion.token)) {
                result = denunciaService.addDenunciaVecino(
                    requestToDenuncia(denuncia.denuncia),
                    vecinoDenunciado
                )
                call.response.status(HttpStatusCode.Created)
                println(
                    "\n--------------------" +
                            "\nSTATUS:DENUNCIA CREATED" +
                            "\n--------------------"
                )
                if (result != null) {
                    call.respond(denunciaToResponse(result))
                }
            } else {
                call.response.status(HttpStatusCode.Unauthorized)
                println(
                    "\n--------------------" +
                            "\nSTATUS:DENUNCIA UNAUTHORIZED" +
                            "\n--------------------"
                )
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            println(
                "\n--------------------" +
                        "\nSTATUS:DENUNCIA BAD REQUEST" +
                        "\n--------------------"
            )
            exposedSQLException.printStackTrace()
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nSTATUS:DENUNCIA INTERNAL SERVER ERROR" +
                        "\n--------------------"
            )
            exception.printStackTrace()
        }
    }

    post("$ruta/subirImagenes/{idDenuncia}") {
        val idDenuncia = call.parameters["idDenuncia"]?.toIntOrNull()
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
                    if (idDenuncia != null) {
                        denunciaService.addImagenToDenuncia(idDenuncia, urlImagen!!)
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
                val cantPaginas = denunciaService.getCantidadPaginas()
                call.response.status(HttpStatusCode.OK)
                call.respond(cantPaginas)
                println(
                    "\n--------------------" +
                            "\nACTOR:PAGINAS DENUNCIA" +
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
                            "\nACTOR:PAGINAS DENUNCIA" +
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
                        "\nACTOR:PAGINAS DENUNCIA" +
                        "\nSTATUS:BAD REQUEST" +
                        "\n--------------------" +
                        "\n${exposedSQLException.message}" +
                        "\n--------------------"
            )
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nACTOR:PAGINAS DENUNCIA" +
                        "\nSTATUS:INTERNAL SERVER ERROR" +
                        "\n--------------------" +
                        "\n${exception.message}" +
                        "\n--------------------"
            )
        }
    }

    get("$ruta/fotos/{idDenuncia}") {
        var resultado: List<String> = ArrayList()
        try {
            val id = call.parameters["idDenuncia"]?.toIntOrNull()
            if (id != null) {
                resultado = denunciaService.getFotos(id)
                call.response.status(HttpStatusCode.OK)
                call.respond(resultado)
                println(
                    "\n--------------------" +
                            "\nACTOR:FOTOS DENUNCIA" +
                            "\nSTATUS:OK" +
                            "\n--------------------"
                )
            } else {
                call.response.status(HttpStatusCode.BadRequest)
                println(
                    "\n--------------------" +
                            "\nACTOR:FOTOS DENUNCIA" +
                            "\nSTATUS:BAD_REQUEST" +
                            "\n--------------------" +
                            "\nEL ID DEL RECLAMO ES:${id}" +
                            "\n--------------------"
                )
            }
        } catch (exposedSQLException: ExposedSQLException) {
            call.response.status(HttpStatusCode.BadRequest)
            println(
                "\n--------------------" +
                        "\nACTOR:FOTOS DENUNCIA" +
                        "\nSTATUS:BAD_REQUEST" +
                        "\n--------------------" +
                        "\n${exposedSQLException.message}" +
                        "\n--------------------"

            )
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            println(
                "\n--------------------" +
                        "\nACTOR:FOTOS DENUNCIA" +
                        "\nSTATUS:INTERNAL_SERVER_ERROR" +
                        "\n--------------------" +
                        "\n${exception.message}" +
                        "\n--------------------"
            )
        }
        call.respond(resultado)
    }

    //TODO IMPLEMENTAR MOVIMIENTOS DENUNCIAS
}

fun requestToVecinoDenunciado(vecinoDenunciado: VecinoDenunciadoRequest): VecinoDenunciado {
    return VecinoDenunciado(
        null,
        null,
        vecinoDenunciado.direccion,
        vecinoDenunciado.nombre,
        vecinoDenunciado.apellido
    )
}

private fun denunciaToResponse(d: Denuncia): DenunciaResponse {
    return DenunciaResponse(
        d.idDenuncia!!,
        d.descripcion,
        d.estado,
        d.aceptarResponsabilidad,
        d.documento)

}

private fun requestToDenuncia(d: DenunciaRequest): Denuncia{
    return Denuncia(
        null,
        d.descripcion,
        null,
        d.aceptarResponsabilidad,
        d.documento
    )
}

fun serializableToComercioDenunciado(comercioDenunciado: ComercioDenunciadoRequest): ComercioDenunciado {
    return ComercioDenunciado(
        comercioDenunciado.idComercio,
        null,
        comercioDenunciado.nombre,
        comercioDenunciado.direccion
    )
}


