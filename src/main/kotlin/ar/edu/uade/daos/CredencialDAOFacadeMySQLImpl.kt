package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Credencial
import ar.edu.uade.models.Vecino.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import ar.edu.uade.models.Credencial.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert

class CredencialDAOFacadeMySQLImpl : CredencialDAOFacade {

    private fun resultRowToCredencial(row: ResultRow) = Credencial(
        documento = row[Credenciales.documento],
        password = row[Credenciales.password],
        id = row[Credenciales.id]
    )

    override suspend fun addNewCredencial(documento: String, password: String): Credencial? = dbQuery{
        val insertStatement = Credenciales.insert {
            it[Credenciales.documento] = documento
            it[Credenciales.password] = password
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToCredencial)
    }

    override suspend fun findCredencialByDocumento(documento: String): Credencial? = dbQuery {
        Credenciales.select { Credenciales.documento like documento }.map(::resultRowToCredencial).singleOrNull()
    }

    val dao: CredencialDAOFacade = CredencialDAOFacadeMySQLImpl().apply {
        runBlocking {
            SchemaUtils.create(Credenciales)

        }
    }
}

