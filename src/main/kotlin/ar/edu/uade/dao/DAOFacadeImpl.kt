package ar.edu.uade.dao

import ar.edu.uade.dao.DAO.dbQuery
import ar.edu.uade.models.Rubro
import ar.edu.uade.models.Rubro.*
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
