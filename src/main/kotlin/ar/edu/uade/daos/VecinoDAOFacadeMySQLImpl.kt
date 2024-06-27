package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Vecino
import ar.edu.uade.models.Vecino.Vecinos
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

class VecinoDAOFacadeMySQLImpl : VecinoDAOFacade {

    private fun resultRowToVecino(row: ResultRow) = Vecino(
        documento = row[Vecinos.documento],
        nombre = row[Vecinos.nombre],
        apellido = row[Vecinos.apellido],
        direccion = row[Vecinos.direccion],
        codigoBarrio = row[Vecinos.codigoBarrio]
    )

    override suspend fun verifyVecino(documento: String): Boolean = dbQuery {
        Vecinos.select { Vecinos.documento like documento }.map(::resultRowToVecino).isNotEmpty()
    }

    override suspend fun findVecino(documento: String): Vecino? = dbQuery {
        Vecinos.select { Vecinos.documento like documento }.map(::resultRowToVecino).singleOrNull()
    }

    override suspend fun getVecinoSegunNomApDir(nombre: String, apellido: String, direccion: String): Vecino?  = dbQuery {
        Vecinos.select { Vecinos.nombre like nombre and(Vecinos.apellido like apellido and(Vecinos.direccion like direccion)) }.map(::resultRowToVecino).singleOrNull()
    }
}