package ar.edu.uade.routes

import ar.edu.uade.mappers.responses.ReclamoResponse
import ar.edu.uade.mappers.serializables.ComercioDenunciadoSerializable
import ar.edu.uade.mappers.serializables.DenunciaSerializable
import ar.edu.uade.mappers.serializables.VecinoDenunciadoSerializable
import ar.edu.uade.models.ComercioDenunciado
import ar.edu.uade.models.Denuncia
import ar.edu.uade.models.VecinoDenunciado
import ar.edu.uade.services.DenunciaService
import ar.edu.uade.services.JWTService
import ar.edu.uade.services.MovimientoReclamoService
import ar.edu.uade.services.ReclamoService
import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.CloudinaryConfig
import ar.edu.uade.utilities.containers.AutenticacionDenunciaComercio
import ar.edu.uade.utilities.containers.AutenticacionDenunciaVecino
import ar.edu.uade.utilities.containers.AutenticacionFiltro
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

fun Route.denunciaRouting(jwtService: JWTService, denunciaService: DenunciaService, /* movimientoDenunciaService: MovimientoDenunciaService ,*/ cloudinaryConfig: CloudinaryConfig) {
    val ruta = "/denuncia"

    put("$ruta/todos/{pagina}") {
        val resultado: MutableList<DenunciaSerializable> = ArrayList<DenunciaSerializable>()
        try {
            val pagina = call.parameters["pagina"]!!.toInt()
            val auth = call.receive<Autenticacion>()
            if (jwtService.validateToken(auth.token)) {
                val denuncias = denunciaService.getDenuncias(pagina)
                for (d in denuncias) {
                    resultado.add(denunciaToSerializable(d))
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
        var resultado: DenunciaSerializable? = null
        try {
            val id = call.parameters["id"]!!.toInt()
            val autenticacion = call.receive<Autenticacion>()
            if (jwtService.validateToken(autenticacion.token)) {
                val denuncia = denunciaService.getDenunciaById(id)
                if (denuncia != null) {
                    resultado = denunciaToSerializable(denuncia)
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

    post("$ruta/nuevoComercio") {
        val result: Denuncia?
        try {
            val request = call.receive<AutenticacionDenunciaComercio>()
            val autenticacion = request.autenticacion
            val denuncia = request.denunciaComercio
            if (jwtService.validateToken(autenticacion.token)) {
                result = denunciaService.addDenunciaComercio(
                    serializableToDenuncia(denuncia.denuncia),
                    serializableToComercioDenunciado(denuncia.comercioDenunciado)
                )
                call.response.status(HttpStatusCode.Created)
                println(
                    "\n--------------------" +
                            "\nSTATUS:DENUNCIA CREATED" +
                            "\n--------------------"
                )
                if (result != null) {
                    call.respond(denunciaToSerializable(result))
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

    post("$ruta/nuevoVecino") {
        val result: Denuncia?
        try {
            val request = call.receive<AutenticacionDenunciaVecino>()
            val autenticacion = request.autenticacion
            val denuncia = request.denunciaVecino
            if (jwtService.validateToken(autenticacion.token)) {
                result = denunciaService.addDenunciaVecino(
                    serializableToDenuncia(denuncia.denuncia),
                    serializableToVecinoDenunciado(denuncia.vecinoDenunciado)
                )
                call.response.status(HttpStatusCode.Created)
                println(
                    "\n--------------------" +
                            "\nSTATUS:DENUNCIA CREATED" +
                            "\n--------------------"
                )
                if (result != null) {
                    call.respond(denunciaToSerializable(result))
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

fun serializableToVecinoDenunciado(vecinoDenunciado: VecinoDenunciadoSerializable): VecinoDenunciado {
    return VecinoDenunciado(
        0,
        vecinoDenunciado.idDenuncia,
        vecinoDenunciado.documento,
        vecinoDenunciado.direccion,
        vecinoDenunciado.nombre,
        vecinoDenunciado.apellido
    )
}

private fun denunciaToSerializable(d: Denuncia): DenunciaSerializable {
    return DenunciaSerializable(
        d.idDenuncia,
        d.descripcion,
        d.estado,
        d.aceptarResponsabilidad,
        d.documento)

}

private fun serializableToDenuncia(d:DenunciaSerializable): Denuncia{
    return Denuncia(
        d.idDenuncia,
        d.descripcion,
        d.estado,
        d.aceptarResponsabilidad,
        d.documento
    )
}

fun serializableToComercioDenunciado(comercioDenunciado: ComercioDenunciadoSerializable): ComercioDenunciado {
    return ComercioDenunciado(0,
        comercioDenunciado.idComercio,
        comercioDenunciado.idDenuncia,
        comercioDenunciado.nombre,
        comercioDenunciado.direccion
    )
}


