package ar.edu.uade.routes

import ar.edu.uade.mappers.MapPublicacion
import ar.edu.uade.mappers.responses.PublicacionResponse
import ar.edu.uade.models.Publicacion
import ar.edu.uade.models.PublicacionImagen
import ar.edu.uade.services.JWTService
import ar.edu.uade.services.PublicacionService
import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.containers.AutenticacionPublicacion
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ar.edu.uade.utilities.CloudinaryConfig
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Route.publicacionRouting(jwtService: JWTService, publicacionService: PublicacionService, cloudinaryConfig: CloudinaryConfig) {
    route("/publicaciones") {
        put("/{pagina}") {
            println("ADENTRO PUBLICACIONES")
            val pagina = call.parameters["pagina"]?.toIntOrNull()
            if (pagina == null) {
                call.respond(HttpStatusCode.BadRequest, "Página inválida")
                return@put
            }
            val publicaciones = publicacionService.getPublicaciones(pagina)
            call.respond(publicaciones)
        }

        put("/cantidadPaginas") {
            val cantidadPaginas = publicacionService.getCantidadPaginas()
            call.respond(cantidadPaginas)
        }

        post("/nuevaPublicacion") {
            var resultado: Publicacion?
            try {
                val body = call.receive<AutenticacionPublicacion>();
                val publicacion = body.publicacion;
                val autenticacion = body.autenticacion;
                println("Autenticacion y Publicacion Separados...")
                if (jwtService.validateToken(autenticacion.token)) {
                    resultado = publicacionService.nuevaPublicacion(publicacion, cloudinaryConfig)
                    call.response.status(HttpStatusCode.Created)
                    println(
                        "\n--------------------" +
                                "\nSTATUS:COMERCIO CREATED" +
                                "\n--------------------"
                    )
                    if (resultado != null) {
                        call.respond(convertToResponse(resultado))
                    }
                } else {
                    call.response.status(HttpStatusCode.Unauthorized)
                    println(
                        "\n--------------------" +
                                "\nSTATUS:PUBLICACION UNAUTHORIZED" +
                                "\n--------------------"
                    )
                }
            } catch (exposedSQLException: ExposedSQLException) {
                call.response.status(HttpStatusCode.BadRequest)
                println(
                    "\n--------------------" +
                            "\nSTATUS:PUBLICACION BAD REQUEST" +
                            "\n--------------------"
                )
                exposedSQLException.printStackTrace()
            } catch (exception: Exception) {
                call.response.status(HttpStatusCode.InternalServerError)
                println(
                    "\n--------------------" +
                            "\nSTATUS:PUBLICACION INTERNAL SERVER ERROR" +
                            "\n--------------------"
                )
                exception.printStackTrace()
            }
        }




        get("/aprobar-publicacion" + "/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            try {
                if (id != null) {
                    publicacionService.aprobarPublicacion(id)
                    call.respond(true)
                } else
                    call.respond(false)
            } catch (e: Exception) {
                call.respond(false)
            }
        }

    }

}
private fun convertToResponse(publicacion: Publicacion): PublicacionResponse {
    return PublicacionResponse(
        id = publicacion.id,
        titulo = publicacion.titulo,
        descripcion = publicacion.descripcion,
        autor = publicacion.autor,
        fechaPublicacion = publicacion.fecha
    )
}

