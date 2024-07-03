package ar.edu.uade.daos

import ar.edu.uade.models.Publicacion
import ar.edu.uade.models.PublicacionImagen
import ar.edu.uade.utilities.Autenticacion

interface PublicacionDAOFacade {
    suspend fun get10Publicaciones(pagina: Int): List<Publicacion>
    suspend fun getPublicacionByID(id: Int): Publicacion?
    suspend fun getAllCantidadPaginas(): Int
    suspend fun nuevaPublicacion(titulo: String, descripcion: String, autor: String, fecha: String, autenticacion: Autenticacion): Publicacion?
    suspend fun subirFotos(foto: PublicacionImagen, id: Int)
    suspend fun getFotos(idPublicacion: Int): List<PublicacionImagen>
}