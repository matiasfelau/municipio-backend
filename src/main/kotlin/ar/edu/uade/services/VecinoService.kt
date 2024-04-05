package ar.edu.uade.services


import ar.edu.uade.daos.CredencialDAOFacade
import ar.edu.uade.daos.CredencialDAOFacadeCacheImpl
import ar.edu.uade.daos.CredencialDAOFacadeMySQLImpl
import io.ktor.server.config.*
import ar.edu.uade.models.Vecino
import java.io.File

class VecinoService(config: ApplicationConfig) {
    private val VecinoDAO: CredencialDAOFacade = CredencialDAOFacadeCacheImpl(
        CredencialDAOFacadeMySQLImpl(),
        File(config.property("storage.ehcacheFilePath").getString())
    ).apply { }

    suspend fun findVecinoByDocumento(documento: String): Vecino? = VecinoDAO.findCredencialByDocumento(documento)
}