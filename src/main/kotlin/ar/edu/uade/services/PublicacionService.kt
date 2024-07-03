package ar.edu.uade.services

import ar.edu.uade.daos.PublicacionDAOFacade
import ar.edu.uade.daos.PublicacionDAOFacadeMySQLImpl
import ar.edu.uade.mappers.MapPublicacion
import ar.edu.uade.models.Publicacion
import ar.edu.uade.models.PublicacionImagen
import ar.edu.uade.utilities.Autenticacion
import io.ktor.server.config.*
import kotlin.math.ceil

class PublicacionService(config: ApplicationConfig) {
    private val dao: PublicacionDAOFacade = PublicacionDAOFacadeMySQLImpl()

    suspend fun getPublicaciones(pagina: Int): List<MapPublicacion> {
        val publicaciones = dao.get10Publicaciones(pagina);
        val lista : MutableList<MapPublicacion> = mutableListOf();
        for(publicacion in publicaciones){
            val p: MapPublicacion = MapPublicacion(publicacion.id,publicacion.titulo, publicacion.descripcion, publicacion.autor, publicacion.fecha, dao.getFotos(publicacion.id))
            lista.add(p);
        }
        return lista;
    }

    suspend fun getPublicacionById(id: Int): Publicacion? {
        return dao.getPublicacionByID(id)
    }

    suspend fun getCantidadPaginas(): Int {
        val cantPaginas = dao.getAllCantidadPaginas()
        val resultado = cantPaginas.toDouble() / 10
        return ceil(resultado).toInt()
    }

    suspend fun nuevaPublicacion(p: MapPublicacion, autenticacion: Autenticacion): Publicacion? {
        val publicacion = dao.nuevaPublicacion(
            p.titulo,
            p.descripcion,
            p.autor,
            p.fecha,
            autenticacion,

        )
        for(s in p.fotos) {
            if (publicacion != null) {
                dao.subirFotos(PublicacionImagen(null, s.url, publicacion.id), publicacion.id)
            };
        }
        return publicacion

    }
}
