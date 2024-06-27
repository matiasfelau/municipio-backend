package ar.edu.uade.services

import ar.edu.uade.daos.VecinoDAOFacade
import ar.edu.uade.daos.VecinoDAOFacadeMySQLImpl
import ar.edu.uade.models.Vecino
import io.ktor.server.config.*

class VecinoService(config: ApplicationConfig) {
    private val vecinoDAO : VecinoDAOFacade = VecinoDAOFacadeMySQLImpl()

    suspend fun getVecino(documento: String) : Vecino? {
        return vecinoDAO.findVecino(documento)
    }

    suspend fun getVecinoSegunNomApDir(nombre: String,apellido: String, direccion: String):Vecino?{
        return vecinoDAO.getVecinoSegunNomApDir(nombre,apellido,direccion)
    }

}