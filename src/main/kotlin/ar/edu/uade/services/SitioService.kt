package ar.edu.uade.services


import ar.edu.uade.daos.SitioDAOFacadeMySQL

import ar.edu.uade.models.Sitio
import io.ktor.server.config.*

class SitioService (config: ApplicationConfig){
    private val dao: SitioDAOFacadeMySQL = SitioDAOFacadeMySQL()

    suspend fun getSitios(): List<Sitio>{
        return dao.getAllSitios()
    }

    suspend fun addSitio(sitio: Sitio): Int?{
        return dao.addNewSitio(sitio)?.idSitio
    }
    suspend fun getSitio(idSitio: Int): Sitio?{
        return dao.getSitioById(idSitio)
    }
}