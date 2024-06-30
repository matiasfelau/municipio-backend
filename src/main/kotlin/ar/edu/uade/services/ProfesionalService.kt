package ar.edu.uade.services

import ar.edu.uade.daos.ProfesionalDAOFacade
import ar.edu.uade.daos.ProfesionalDAOFacadeMySQLImpl
import ar.edu.uade.mappers.MapImagenProfesional
import ar.edu.uade.mappers.MapProfesional
import ar.edu.uade.models.Profesional
import ar.edu.uade.utilities.CloudinaryConfig
import com.cloudinary.utils.ObjectUtils
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil

class ProfesionalService {
    private val dao: ProfesionalDAOFacade = ProfesionalDAOFacadeMySQLImpl()

    suspend fun getCantidadPaginas(): Int {
        return ceil(dao.getCantidadElementos().toDouble() / 10).toInt()
    }

    suspend fun get10Profesionales(pagina: Int): MutableList<MapProfesional> {
        val profesionales = dao.get10Profesionales(pagina)
        val mapsProfesionales: MutableList<MapProfesional> = ArrayList<MapProfesional>();
        for (profesional in profesionales) {
            val mapsImagenes: MutableList<String> = ArrayList<String>();
            val imagenes = dao.getFotos(profesional.idProfesional)
            for (imagen in imagenes) {
                mapsImagenes.add(imagen.urlImagen)
            }
            mapsProfesionales.add(MapProfesional(
                profesional.nombre,
                profesional.direccion,
                profesional.telefono,
                profesional.email,
                profesional.latitud,
                profesional.longitud,
                profesional.inicioJornada,
                profesional.finJornada,
                profesional.documento,
                mapsImagenes
            ))
        }
        return mapsProfesionales
    }

    suspend fun addProfesional(mapProfesional: MapProfesional, cloudinaryConfig: CloudinaryConfig): Profesional? {
        val profesional: Profesional? = dao.addProfesional(
            mapProfesional.nombre,
            mapProfesional.direccion,
            mapProfesional.telefono,
            mapProfesional.email,
            mapProfesional.latitud,
            mapProfesional.longitud,
            mapProfesional.inicioJornada,
            mapProfesional.finJornada,
            mapProfesional.documento
        )
        if (profesional != null) {
            val idProfesional: Int = profesional.idProfesional
            println(mapProfesional.images.get(0))
            for (image in mapProfesional.images) {
                val imageBytes = Base64.getDecoder().decode(image)
                val uploadResult = cloudinaryConfig.cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap())
                val imageUrl = uploadResult["url"] as String
                dao.addImagenToProfesional(
                    imageUrl,
                    idProfesional
                )
            }
        }
        return profesional
    }

    suspend fun habilitarProfesional(idProfesional: Int): Boolean {
        return dao.habilitarProfesional(idProfesional)
    }
}