package ar.edu.uade.routes

import ar.edu.uade.mappers.requests.ComercioDenunciadoRequest
import ar.edu.uade.mappers.requests.DenunciaRequest
import ar.edu.uade.mappers.requests.VecinoDenunciadoRequest
import ar.edu.uade.mappers.responses.DenunciaResponse
import ar.edu.uade.mappers.responses.DenunciadoResponse
import ar.edu.uade.mappers.responses.MovimientoDenunciaResponse
import ar.edu.uade.mappers.responses.MovimientoReclamoResponse
import ar.edu.uade.models.*
import ar.edu.uade.services.*
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

fun Route.denunciaRouting(jwtService: JWTService, denunciaService: DenunciaService, vecinoService: VecinoService, comercioService: ComercioService, movimientoDenunciaService: MovimientoDenunciaService, cloudinaryConfig: CloudinaryConfig) {
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
            val denunciaContainer = request.containerDenunciaComercio
            print("Nombre comercio : ${denunciaContainer.comercioDenunciado.nombre}")
            denunciaContainer.denuncia.estado = "Nuevo"
            print("direccion comercio : ${denunciaContainer.comercioDenunciado.direccion}")
            val idcomercioDenunciado = comercioService.getComercioByNomYDir(denunciaContainer.comercioDenunciado.nombre, denunciaContainer.comercioDenunciado.direccion)
            val comercioDenunciadoEncontrado = serializableToComercioDenunciado(denunciaContainer.comercioDenunciado, idcomercioDenunciado)
            println("COMERCIO DENUNCIADO ES ID: $idcomercioDenunciado")
            if (jwtService.validateToken(autenticacion.token)) {
                if(idcomercioDenunciado != Int.MAX_VALUE){
                    result = denunciaService.addDenunciaComercio(
                        requestToDenuncia(denunciaContainer.denuncia),
                        comercioDenunciadoEncontrado
                    )

                    var files = denunciaContainer.denuncia.fileStrings!!
                    for(imagen in files){
                        if (result != null) {
                            result.idDenuncia?.let { it1 -> denunciaService.addFileToDenuncia(it1,imagen,cloudinaryConfig) }
                        }
                    }

                    call.response.status(HttpStatusCode.Created)
                    println(
                        "\n--------------------" +
                                "\nSTATUS:DENUNCIA CREATED" +
                                "\n--------------------"
                    )
                    if (result != null) {
                        call.respond(denunciaToResponse(result))
                    }
                }  else{
                    call.response.status(HttpStatusCode.NotFound)
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

        val result: Denuncia?
        try {
            val request = call.receive<AutenticacionDenunciaVecino>()
            val autenticacion = request.autenticacion
            val denunciaContainer = request.denuncia
            denunciaContainer.denuncia.estado = "Nuevo"
            val vecinoDenunciado = requestToVecinoDenunciado(denunciaContainer.vecinoDenunciado)
            //implementacion de busqueda de denunciado
            vecinoDenunciado.documento = vecinoService.getVecinoSegunNomApDir(vecinoDenunciado.nombre, vecinoDenunciado.apellido, vecinoDenunciado.direccion)?.documento
            if (jwtService.validateToken(autenticacion.token)) {
                result = denunciaService.addDenunciaVecino(
                    requestToDenuncia(denunciaContainer.denuncia),
                    vecinoDenunciado
                )
                val files = denunciaContainer.denuncia.fileStrings!!
                for(file in files){
                    if (result != null) {
                        result.idDenuncia?.let { it1 -> denunciaService.addFileToDenuncia(it1,file,cloudinaryConfig) }
                    }
                }

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

    /*post("$ruta/subirImagenes/{idDenuncia}") {
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
                        denunciaService.addFileToDenuncia(idDenuncia, urlImagen!!)
                    }
                }
                file.delete()
            }
            part.dispose()
        }
    }*/

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

    get("$ruta/movimientos/{idDenuncia}") {
        try{
            val movimientosReclamo = call.parameters["IdDenuncia"]?.toIntOrNull()
                ?.let { it1 -> movimientoDenunciaService.getMovimientosDenuncia(it1) }
            var resultado: MutableList<MovimientoDenunciaResponse> = ArrayList<MovimientoDenunciaResponse>()
            if (movimientosReclamo != null) {
                for (m in movimientosReclamo){
                    resultado.add(movimientoToResponse(m))

                }
                call.response.status(HttpStatusCode.OK)
                println("BRODE UHH")
                println(resultado)
                call.respond(resultado)
            }
            else {
                call.response.status(HttpStatusCode.BadRequest)
                println("BRODE UHH 2")
            }

        }catch (nullexcedException: NullPointerException){
            call.response.status(HttpStatusCode.BadRequest)
            println("\n--------------------" +
                    "\nACTOR:MOVIMIENTOS RECLAMO" +
                    "\nSTATUS:BAD_REQUEST" +
                    "\n--------------------" +
                    "\n${nullexcedException.message}" +
                    "\n--------------------"

            )
        }catch (ex: ExposedSQLException){
            call.response.status(HttpStatusCode.BadRequest)
            println("\n--------------------" +
                    "\nACTOR:MOVIMIENTOS RECLAMO" +
                    "\nSTATUS:BAD_REQUEST" +
                    "\n--------------------" +
                    "\n${ex.message}" +
                    "\n--------------------"

            )
        }catch(numberFormatException: NumberFormatException){
            call.response.status(HttpStatusCode.BadRequest)
            println("\n--------------------" +
                    "\nACTOR:MOVIMIENTOS RECLAMO" +
                    "\nSTATUS:BAD_REQUEST" +
                    "\n--------------------" +
                    "\n${numberFormatException.message}" +
                    "\n--------------------"

            )
        }catch (ex: Exception){
            call.response.status(HttpStatusCode.InternalServerError)
            println("\n--------------------" +
                    "\nACTOR:MOVIMIENTOS RECLAMO" +
                    "\nSTATUS:INTERNAL_SERVER_ERROR" +
                    "\n--------------------" +
                    "\n${ex.message}" +
                    "\n--------------------"
            )
        }
    }
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

fun serializableToComercioDenunciado(comercioDenunciado: ComercioDenunciadoRequest, id: Int): ComercioDenunciado {
    return ComercioDenunciado(
        id,
        null,
        comercioDenunciado.nombre,
        comercioDenunciado.direccion
    )
}

private fun movimientoToResponse(movimientoDenuncia: MovimientoDenuncia): MovimientoDenunciaResponse {
    return MovimientoDenunciaResponse(
        movimientoDenuncia.idMovimiento,
        movimientoDenuncia.responsable,
        movimientoDenuncia.causa,
        movimientoDenuncia.fecha,
        movimientoDenuncia.idDenuncia
    )
}


