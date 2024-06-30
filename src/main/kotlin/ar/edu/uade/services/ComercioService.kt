package ar.edu.uade.services

import ar.edu.uade.daos.ComercioDAOFacade
import ar.edu.uade.daos.ComercioDAOFacadeMySQLImpl
import ar.edu.uade.mappers.MapComercio
import ar.edu.uade.mappers.MapProfesional
import ar.edu.uade.models.Comercio
import ar.edu.uade.models.Denuncia
import ar.edu.uade.models.Profesional
import ar.edu.uade.utilities.CloudinaryConfig
import com.cloudinary.utils.ObjectUtils
import io.ktor.server.config.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil

class ComercioService(config: ApplicationConfig) {
    private val dao: ComercioDAOFacade = ComercioDAOFacadeMySQLImpl()

    suspend fun getComercios(pagina: Int): List<Comercio>{
        return dao.get10Comercio(pagina)
    }

    suspend fun getComercioById(id: Int): Comercio? {
        return  dao.getComercioByID(id)
    }

    suspend fun get10Comercios(pagina: Int): List<Comercio> {
        return dao.get10Comercio(pagina)
    }

    suspend fun getCantidadPaginas(): Int {
        val cantpaginas = dao.getallCantidadPaginas()
        val resultado = cantpaginas.toDouble()/10
        return ceil(resultado).toInt()
    }

    suspend fun addComercio(mapComercio: MapComercio, cloudinaryConfig: CloudinaryConfig) : Comercio? {
        val comercio: Comercio? = dao.addComercio(
            mapComercio.nombre,
            mapComercio.documento,
            mapComercio.direccion,
            mapComercio.descripcion,
            mapComercio.telefono,
            mapComercio.apertura,
            mapComercio.cierre,
            mapComercio.latitud,
            mapComercio.longitud
        )
        if (comercio != null){
            val idComercio: Int = comercio.idComercio
            println(mapComercio.images.get(0))
            for (image in mapComercio.images){
                val imageBytes = Base64.getDecoder().decode(image)
                val uploadResult = cloudinaryConfig.cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap())
                val imageUrl = uploadResult["url"] as String
                dao.addImagenToComercio(
                    imageUrl,
                    idComercio
                )
            }
        }
        return comercio
    }

    suspend fun getFotos(id: Int): MutableList<String> {
        val resultado: MutableList<String> = ArrayList()
        for (foto in dao.getFotosById(id)) {
            resultado.add(foto.urlImagen)
        }
        return resultado
    }

    suspend fun getComercioByNomYDir(nombre: String, direccion: String): Int {
        val result = dao.getComercioByNomYDir(nombre, direccion)
        if (result != null){
            return result.idComercio
        }
        return Int.MAX_VALUE

    }
}