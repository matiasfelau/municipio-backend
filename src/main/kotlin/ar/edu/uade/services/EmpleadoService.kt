package ar.edu.uade.services

import ar.edu.uade.models.Empleado
import ar.edu.uade.daos.EmpleadoDAOFacade
import ar.edu.uade.daos.EmpleadoDAOFacadeMySQLImpl
import io.ktor.server.config.*
import java.io.File

class EmpleadoService(config: ApplicationConfig) {
    private val empleadoDAO: EmpleadoDAOFacade = EmpleadoDAOFacadeMySQLImpl()

    suspend fun findEmpleadoByLegajo(legajo: Int): Empleado? = empleadoDAO.findEmpleadoByLegajo(legajo)
}