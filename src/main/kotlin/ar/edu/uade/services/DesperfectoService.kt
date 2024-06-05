package ar.edu.uade.services

import ar.edu.uade.daos.DesperfectoDAOFacade
import ar.edu.uade.daos.DesperfectoDAOFacadeMySQLImpl
import ar.edu.uade.models.Desperfecto
import ar.edu.uade.models.Rubro
import io.ktor.server.config.*

class DesperfectoService(config: ApplicationConfig) {
    private val dao: DesperfectoDAOFacade = DesperfectoDAOFacadeMySQLImpl()

    suspend fun getDesperfectos(): List<Desperfecto>{
        return dao.getAllDesperfectos()
    }
}