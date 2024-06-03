package ar.edu.uade.services

import ar.edu.uade.daos.ReclamoDAOFacade
import ar.edu.uade.daos.ReclamoDAOFacadeMySQLImpl
import ar.edu.uade.models.Reclamo
import ar.edu.uade.utilities.Filtro
import io.ktor.server.config.*

class ReclamoService(config: ApplicationConfig) {
    private val dao: ReclamoDAOFacade = ReclamoDAOFacadeMySQLImpl()

    suspend fun getReclamosByFiltro(pagina: Int, filtro: Filtro): List<Reclamo> {
        val reclamos = when (filtro.tipo) {
            "" -> dao.get10Reclamos(pagina)
            "sector" -> dao.get10ReclamosBySector(pagina, filtro.dato)
            "documento" -> dao.get10ReclamosByDocumento(pagina, filtro.dato)
            else -> throw NoSuchMethodException("Éste es el error.")
        }
        return reclamos
    }

    suspend fun getReclamoById(id: Int): Reclamo? {
        return dao.getReclamoById(id)
    }
}