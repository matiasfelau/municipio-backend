package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Desperfecto
import ar.edu.uade.models.Desperfecto.*

import org.jetbrains.exposed.sql.ResultRow

import org.jetbrains.exposed.sql.selectAll

class DesperfectoDAOFacadeMySQL: DesperfectoDAOFacade {

    private fun resultRowToDesperfecto(row: ResultRow) = Desperfecto(
        idDesperfecto = row[Desperfectos.idDesperfecto],
        descripcion = row[Desperfectos.descripcion],
        idRubro = row[Desperfectos.idRubro]
    )


    override suspend fun getAllDesperfectos(): List<Desperfecto>  = dbQuery {
        Desperfectos.selectAll().map(::resultRowToDesperfecto)
    }

}
