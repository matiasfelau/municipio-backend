package ar.edu.uade.repository

import ar.edu.uade.repository.DatabaseSingleton.dbQuery
import ar.edu.uade.model.Rubro
import ar.edu.uade.model.Rubro.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class RubroDAOFacadeImpl : RubroDAOFacade {

    private fun resultRowToRubro(row: ResultRow) = Rubro(
        idRubro = row[Rubros.idRubro],
        descripcion = row[Rubros.descripcion]
    )

    override suspend fun allRubros(): List<Rubro> = dbQuery {
        Rubros.selectAll().map(::resultRowToRubro)
    }
}

//val rubroDao: RubroDAOFacade = RubroDAOFacadeImpl().apply {    }
