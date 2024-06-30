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
import kotlin.math.ceil

class ProfesionalService {
    private val dao: ProfesionalDAOFacade = ProfesionalDAOFacadeMySQLImpl()

    suspend fun getCantidadPaginas(): Int {
        return ceil(dao.getCantidadElementos().toDouble() / 10).toInt()
    }

    suspend fun get10Profesionales(pagina: Int): List<Profesional> {
        return dao.get10Profesionales(pagina)
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
}