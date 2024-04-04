package ar.edu.uade.daos


import ar.edu.uade.models.Reclamo
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import ar.edu.uade.databases.MySQLSingleton.dbQuery

class ReclamoDAOFacadeMySQLImpl: ReclamoDAOFacade{

    private fun resultRowToReclamo(row: ResultRow) = Reclamo(
        idReclamo = row[Reclamo.Reclamos.idReclamo],
        documento = row[Reclamo.Reclamos.documento],
        idSitio = row[Reclamo.Reclamos.idSitio],
        idDesperfecto = row[Reclamo.Reclamos.idDesperfecto],
        descripcion = row[Reclamo.Reclamos.descripcion],
        estado = row[Reclamo.Reclamos.estado],
        idReclamoUnificado = row[Reclamo.Reclamos.idReclamoUnificado]
    )
    override suspend fun findReclamoById(idReclamo: Int): Reclamo? = dbQuery {
        Reclamo.Reclamos.select { Reclamo.Reclamos.idReclamo eq idReclamo }.map(::resultRowToReclamo).singleOrNull()
    }
}