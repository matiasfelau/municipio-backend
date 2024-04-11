package ar.edu.uade.services


import ar.edu.uade.daos.CredencialDAOFacade
import ar.edu.uade.daos.CredencialDAOFacadeCacheImpl
import ar.edu.uade.daos.CredencialDAOFacadeMySQLImpl
import ar.edu.uade.models.Credencial
import io.ktor.server.config.*
import ar.edu.uade.models.Vecino
import java.io.File

class CredencialService(config: ApplicationConfig) {
    private val credencialDAO: CredencialDAOFacade = CredencialDAOFacadeCacheImpl(
        CredencialDAOFacadeMySQLImpl(),
        File(config.property("storage.ehcacheFilePath").getString())
    ).apply { }

    suspend fun findCredencialByDocumento(documento: String): Credencial? = credencialDAO.findCredencialByDocumento(documento)

    suspend fun addNewCredencial(documento:String, password: String): Credencial? = credencialDAO.addNewCredencial(documento,password)
}