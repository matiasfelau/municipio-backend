package ar.edu.uade.services


import ar.edu.uade.daos.VecinoDAOFacade
import ar.edu.uade.daos.VecinoDAOFacadeCacheImpl
import ar.edu.uade.daos.VecinoDAOFacadeMySQLImpl
import io.ktor.server.config.*
import ar.edu.uade.models.Vecino
import java.io.File

class VecinoService(config: ApplicationConfig) {
    private val VecinoDAO: VecinoDAOFacade = VecinoDAOFacadeCacheImpl(
        VecinoDAOFacadeMySQLImpl(),
        File(config.property("storage.ehcacheFilePath").getString())
    ).apply { }

    suspend fun findVecinoByDocumento(documento: String): Vecino? = VecinoDAO.findVecinoByDocumento(documento)
}