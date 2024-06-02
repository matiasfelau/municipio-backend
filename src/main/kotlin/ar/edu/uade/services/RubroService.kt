package ar.edu.uade.services

import ar.edu.uade.daos.RubroDAOFacadeMySQL
import ar.edu.uade.models.Desperfecto
import ar.edu.uade.models.Rubro
import io.ktor.server.config.*

class RubroService(config: ApplicationConfig) {
    private val dao: RubroDAOFacadeMySQL = RubroDAOFacadeMySQL()

    suspend fun getRubros(): List<Rubro>{
        return dao.getAllRubros()
    }
}