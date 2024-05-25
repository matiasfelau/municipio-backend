package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Barrio.Barrios
import ar.edu.uade.models.Credencial
import ar.edu.uade.models.Credencial.Credenciales
import ar.edu.uade.models.Vecino
import ar.edu.uade.models.Vecino.Vecinos
import ar.edu.uade.models.Vecino.Vecinos.integer
import ar.edu.uade.models.Vecino.Vecinos.references
import ar.edu.uade.models.Vecino.Vecinos.uniqueIndex
import ar.edu.uade.models.Vecino.Vecinos.varchar
import kotlinx.coroutines.selects.select
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.select

class VecinoDAOFacadeMySQLImpl : VecinoDAOFacade {

    private fun resultRowToVecino(row: ResultRow) = Vecino(
        documento = row[Vecinos.documento],
        nombre = row[Vecinos.nombre],
        apellido = row[Vecinos.apellido],
        direccion = row[Vecinos.direccion],
        codigoDeBarrio = row[Vecinos.codigoDeBarrio]
    )

    override suspend fun verifyVecino(documento: String): Boolean = dbQuery {
        Vecinos.select { Vecinos.documento like documento }.map(::resultRowToVecino).isNotEmpty()
    }
}