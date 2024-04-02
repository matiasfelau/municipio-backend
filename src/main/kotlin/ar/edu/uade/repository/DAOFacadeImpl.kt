package ar.edu.uade.repository

import ar.edu.uade.repository.DAO.dbQuery
import ar.edu.uade.model.Rubro
import ar.edu.uade.model.Rubro.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class DAOFacadeImpl : DAOFacade {
    private fun resultRowToRubro(row: ResultRow) = Rubro(
        idRubro = row[Rubros.idRubro],
        descripcion = row[Rubros.descripcion]
    )
    override suspend fun allRubros(): List<Rubro> = dbQuery {
        Rubros.selectAll().map(::resultRowToRubro)
    }
}
val dao: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        if(allRubros().isEmpty()) {
            true
        }
    }
}
