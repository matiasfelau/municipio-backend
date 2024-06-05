package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Desperfecto
import ar.edu.uade.models.Desperfecto.Desperfectos
import ar.edu.uade.models.Rubro
import ar.edu.uade.models.Rubro.Rubros
import org.bouncycastle.jcajce.provider.symmetric.DES
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class DesperfectoDAOFacadeMySQLImpl: DesperfectoDAOFacade {

    private fun resultRowToDesperfecto(row: ResultRow) = Desperfecto(
        idDesperfecto = row[Desperfectos.idDesperfecto],
        descripcion = row[Desperfectos.descripcion],
        idRubro = row[Desperfectos.idRubro]
    )

    override suspend fun getAllDesperfectos(): List<Desperfecto>  = dbQuery {
        Desperfectos.selectAll().map(::resultRowToDesperfecto)
    }
}