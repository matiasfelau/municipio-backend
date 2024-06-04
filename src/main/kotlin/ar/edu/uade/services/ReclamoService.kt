package ar.edu.uade.services

import ar.edu.uade.daos.ReclamoDAOFacade
import ar.edu.uade.daos.ReclamoDAOFacadeMySQLImpl
import ar.edu.uade.models.Reclamo
import ar.edu.uade.utilities.CloudinaryConfig
import ar.edu.uade.utilities.Filtro
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import io.ktor.server.config.*
import kotlinx.html.MATH
import java.io.File
import javax.mail.Multipart
import kotlin.math.ceil

class ReclamoService(config: ApplicationConfig) {
    private val dao: ReclamoDAOFacade = ReclamoDAOFacadeMySQLImpl()

    suspend fun getReclamosByFiltro(pagina: Int, filtro: Filtro): List<Reclamo> {
        val reclamos = when (filtro.tipo) {
            "" -> dao.get10Reclamos(pagina)
            "sector" -> dao.get10ReclamosBySector(pagina, filtro.dato)
            "documento" -> dao.get10ReclamosByDocumento(pagina, filtro.dato)
            else -> throw NoSuchMethodException("No existe un método para el filtro solicitado.")
        }
        return reclamos
    }

    suspend fun getReclamoById(id: Int): Reclamo? {
        return dao.getReclamoById(id)
    }

    suspend fun createReclamo(requestToReclamo: Reclamo) {
        dao.addReclamo(requestToReclamo)
    }

    suspend fun addImageToReclamo(file: File, urlElemento: String){
        CloudinaryConfig.cloudinary.uploader().upload(file, ObjectUtils.emptyMap())
    }

    suspend fun getCantidadPaginas(filtro: Filtro): Int {
        val cantpaginas = when (filtro.tipo) {
            "" -> dao.getAllCantidadPaginas()
            "sector" -> dao.getAllCantidadPaginasBySector(filtro.dato)
            "documento" -> dao.getAllCantidadPaginasByDocumento(filtro.dato)
            else -> throw NoSuchMethodException("No existe un método para el filtro solicitado.")
        }
        println(cantpaginas)
        val resultado = cantpaginas.toDouble()/10
        println(resultado)
        return ceil(resultado).toInt()
    }
}