package ar.edu.uade.daos

import ar.edu.uade.models.Empleado

interface EmpleadoDAOFacade {
    suspend fun findEmpleadoByLegajo(legajo: Int): Empleado?
}
