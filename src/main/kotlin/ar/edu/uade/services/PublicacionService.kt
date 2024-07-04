package ar.edu.uade.services

import ar.edu.uade.daos.PublicacionDAOFacade
import ar.edu.uade.daos.PublicacionDAOFacadeMySQLImpl
import ar.edu.uade.mappers.MapPublicacion
import ar.edu.uade.mappers.MapPublicacionImagen
import ar.edu.uade.models.Publicacion
import ar.edu.uade.models.PublicacionImagen
import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.CloudinaryConfig
import com.cloudinary.utils.ObjectUtils
import io.ktor.server.config.*
import java.util.*
import kotlin.math.ceil

class PublicacionService(config: ApplicationConfig) {
    private val dao: PublicacionDAOFacade = PublicacionDAOFacadeMySQLImpl()

    suspend fun getPublicaciones(pagina: Int): List<MapPublicacion> {
        val publicaciones = dao.get10Publicaciones(pagina);
        val lista : MutableList<MapPublicacion> = mutableListOf();
        for(publicacion in publicaciones){
            val fotos = dao.getFotos(publicacion.id)
            val f: MutableList<String> = mutableListOf()
            for (foto in fotos){
                f.add(foto.url)
            }
            val p: MapPublicacion = MapPublicacion(
                publicacion.titulo,
                publicacion.descripcion,
                publicacion.autor,
                publicacion.fecha,
                f)
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

    suspend fun nuevaPublicacion(p: MapPublicacion, cloudinaryConfig: CloudinaryConfig): Publicacion? {
        val publicacion = dao.nuevaPublicacion(
            p.titulo,
            p.descripcion,
            p.autor,
            p.fecha

        )
        if (publicacion != null){
            val idPublicacion: Int = publicacion.id
            println(p.fotos.get(0))
            for (image in p.fotos){
                val imageBytes = Base64.getDecoder().decode(image)
                val uploadResult = cloudinaryConfig.cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap())
                val imageUrl = uploadResult["url"] as String
                dao.addFotoPublicacion(
                    imageUrl,
                    idPublicacion
                )
            }
        }
        return publicacion

    }

    suspend fun aprobarPublicacion(id: Int) {
        dao.aprobarPublicacion(id)
    }
}
