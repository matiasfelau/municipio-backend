package ar.edu.uade.services


import ar.edu.uade.daos.CredencialDAOFacade
import ar.edu.uade.daos.CredencialDAOFacadeMySQLImpl
import ar.edu.uade.models.Credencial
import io.ktor.server.config.*
import kotlin.random.Random

class CredencialService(config: ApplicationConfig) {
    private val credencialDAO : CredencialDAOFacade = CredencialDAOFacadeMySQLImpl()
    private val vecinoDAO : VecinoDAOFacade = VecinoDAOFacadeMySQLImpl()

    suspend fun solicitarCredencial(documento: String, email: String) : Boolean {
        var bool = false
        if (vecinoDAO.verifyVecino(documento)) {
            if (credencialDAO.addNewCredencial(documento, email) != null) {
                bool = true
            }
        }
        return bool
    }

    suspend fun habilitarCredencial(credencial: Credencial) {
        if (credencialDAO.findCredencialByDocumento(credencial.documento)?.habilitado == false) {
            val password = Random.nextInt(10000000, 99999999).toString()
            val habilitado = true
            val primerIngreso = true
            if (credencialDAO.editHabilitadoCredencial(credencial.documento, password, habilitado, primerIngreso)) {
                //TODO notificar
            }
        }
    }

    suspend fun clavePrimerIngresoCredencial(credencial: Credencial) : Boolean {
        val bd = credencialDAO.findCredencialByDocumento(credencial.documento)
        var bool = false
        if (bd != null) {
            if (bd.habilitado && bd.primerIngreso) {
                var password = bd.password
                val primerIngreso = false
                if (credencial.password != password) {
                    password = credencial.password
                }
                bool = credencialDAO.editPasswordCredencial(credencial.documento, password, primerIngreso)
            }
        }
        return bool
    }

    suspend fun find(documento: String): Credencial? {
        return credencialDAO.findCredencialByDocumento(documento)
    }
}