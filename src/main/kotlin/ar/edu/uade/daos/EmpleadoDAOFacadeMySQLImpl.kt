package ar.edu.uade.daos

import ar.edu.uade.models.Empleado
import ar.edu.uade.models.Empleado.Personal
import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.security.hash
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class EmpleadoDAOFacadeMySQLImpl : EmpleadoDAOFacade {

    private fun resultRowToPersonal(row: ResultRow) = Empleado(
        legajo = row[Personal.legajo],
        nombre = row[Personal.nombre],
        apellido = row[Personal.apellido],
        password = row[Personal.password],
        sector = row[Personal.sector],
        categoria = row[Personal.categoria],
        fechaIngreso = row[Personal.fechaIngreso]
    )

    override suspend fun findEmpleadoByLegajo(legajo: Int): Empleado? = dbQuery {
        Personal.select { Personal.legajo eq legajo }.map(::resultRowToPersonal).singleOrNull()
    }
}

