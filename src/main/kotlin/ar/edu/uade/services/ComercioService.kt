package ar.edu.uade.services

import ar.edu.uade.daos.ComercioDAOFacade
import ar.edu.uade.daos.ComercioDAOFacadeMySQLImpl
import ar.edu.uade.models.Comercio
import ar.edu.uade.models.Denuncia
import io.ktor.server.config.*
import kotlin.math.ceil

class ComercioService() {
    private val dao: ComercioDAOFacade = ComercioDAOFacadeMySQLImpl()

    suspend fun getComercios(pagina: Int): List<Comercio>{
        return dao.get10Comercio(pagina)
    }

    suspend fun getComercioById(id: Int): Comercio? {
        return  dao.getComercioByID(id)
    }

    suspend fun getCantidadPaginas(): Int {
        val cantpaginas = dao.getallCantidadPaginas()
        val resultado = cantpaginas.toDouble()/10
        return ceil(resultado).toInt()
    }

    suspend fun addComercio(requestToComercio: Comercio) : Comercio? {
        return dao.addComercio(requestToComercio)
    }

    suspend fun  addImageToComercio(idComercio: Int, urlImagen : String){
        dao.addImagenToComercio(idComercio,urlImagen)
    }

    suspend fun getFotos(id: Int): MutableList<String> {
        val resultado: MutableList<String> = ArrayList()
        for (foto in dao.getFotosById(id)) {
            resultado.add(foto.urlImagen)
        }
        return resultado
    }
}