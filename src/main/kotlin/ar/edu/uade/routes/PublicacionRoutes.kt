package ar.edu.uade.routes

import ar.edu.uade.mappers.MapPublicacion
import ar.edu.uade.mappers.responses.PublicacionResponse
import ar.edu.uade.models.Publicacion
import ar.edu.uade.models.PublicacionImagen
import ar.edu.uade.services.PublicacionService
import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.containers.AutenticacionPublicacion
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.publicacionRouting(publicacionService: PublicacionService) {
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
            val body = call.receive<AutenticacionPublicacion>();
            val publicacion = body.publicacion;
            val autenticacion = body.autenticacion;
            try {
                val nuevaPublicacion = publicacionService.nuevaPublicacion(publicacion, autenticacion)
                nuevaPublicacion?.let { it1 -> convertToResponse(it1) }?.let { it2 -> call.respond(it2) }
            } catch (e: IllegalStateException) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error al crear la publicación")
            }
        }
    }
}
private fun convertToResponse( publicacion: Publicacion): PublicacionResponse {
    return PublicacionResponse(
        id = publicacion.id,
        titulo = publicacion.titulo,
        descripcion = publicacion.descripcion,
        autor = publicacion.autor,
        fechaPublicacion = publicacion.fecha,
        imagenes = publicacion.imagenes,
    )
}

