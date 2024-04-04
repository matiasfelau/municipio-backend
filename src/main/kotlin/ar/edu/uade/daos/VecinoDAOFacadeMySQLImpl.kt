package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Vecino
import ar.edu.uade.models.Vecino.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

class VecinoDAOFacadeMySQLImpl : VecinoDAOFacade {

    private fun resultRowToVecinos(row: ResultRow) = Vecino(
        documento = row[Vecinos.documento],
        password = row[Vecinos.password],
        nombre = row[Vecinos.nombre],
        apellido = row[Vecinos.apellido],
        direccion = row[Vecinos.direccion],
        codigoDeBarrio = row[Vecinos.codigoDeBarrio]
    )

    override suspend fun findVecinoByDocumento(documento: String): Vecino? = dbQuery {
        Vecinos.select { Vecinos.documento like documento }.map(::resultRowToVecinos).singleOrNull()
    }
}
