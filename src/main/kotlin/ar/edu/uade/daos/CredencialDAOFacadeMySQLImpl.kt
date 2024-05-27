package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Credencial
import ar.edu.uade.models.Vecino.*
import ar.edu.uade.models.Credencial.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*

class CredencialDAOFacadeMySQLImpl : CredencialDAOFacade {

    private fun resultRowToCredencial(row: ResultRow) = Credencial(
        documento = row[Credenciales.documento],
        password = row[Credenciales.password],
        email = row[Credenciales.email],
        habilitado = row[Credenciales.habilitado],
        primerIngreso = row[Credenciales.primerIngreso]
    )

    override suspend fun addNewCredencial(documento: String, email: String): Credencial? = dbQuery {
        val insertStatement = Credenciales.insert {
            it[Credenciales.documento] = documento
            it[Credenciales.email] = email
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToCredencial)
    }

    override suspend fun editPrimerIngresoCredencial(documento: String, password: String, primerIngreso: Boolean): Boolean = dbQuery {
        Credenciales.update({ Credenciales.documento eq documento }) {
            it[Credenciales.documento] = documento
            it[Credenciales.password] = password
            it[Credenciales.primerIngreso] = primerIngreso
        } > 0
    }

    override suspend fun findCredencialByDocumento(documento: String): Credencial? = dbQuery {
        Credenciales.select { Credenciales.documento like documento }.map(::resultRowToCredencial).singleOrNull()
    }

    override suspend fun editHabilitadoCredencial(documento: String, password: String, habilitado: Boolean, primerIngreso: Boolean): Boolean = dbQuery {
        Credenciales.update({ Credenciales.documento eq documento }) {
            it[Credenciales.password] = password
            it[Credenciales.habilitado] = habilitado
            it[Credenciales.primerIngreso] = primerIngreso
        } > 0
    }
}

