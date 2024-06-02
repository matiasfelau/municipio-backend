package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Rubro
import ar.edu.uade.models.Rubro.*

import org.jetbrains.exposed.sql.ResultRow

import org.jetbrains.exposed.sql.selectAll

class RubroDAOFacadeMySQL: RubroDAOFacade {

    private fun resultRowToRubro(row: ResultRow) = Rubro(
        idRubro = row[Rubro.Rubros.idRubro],
        descripcion = row[Rubro.Rubros.descripcion]
    )


    override suspend fun getAllRubros(): List<Rubro>  = dbQuery {
        Rubros.selectAll().map(::resultRowToRubro)
    }

}
