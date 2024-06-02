package ar.edu.uade.services

import ar.edu.uade.daos.DesperfectoDAOFacadeMySQL
import ar.edu.uade.daos.ReclamoDAOFacade
import ar.edu.uade.daos.ReclamoDAOFacadeMySQLImpl
import ar.edu.uade.models.Desperfecto
import io.ktor.server.config.*

class DesperfectoService(config: ApplicationConfig) {
    private val dao: DesperfectoDAOFacadeMySQL = DesperfectoDAOFacadeMySQL()

    suspend fun getDesperfectos(): List<Desperfecto>{
        return dao.getAllDesperfectos()
    }
}