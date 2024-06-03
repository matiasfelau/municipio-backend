package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Desperfecto.Desperfectos
import ar.edu.uade.models.Reclamo
import ar.edu.uade.models.Reclamo.Reclamos
import ar.edu.uade.models.Rubro.Rubros
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class ReclamoDAOFacadeMySQLImpl: ReclamoDAOFacade {
    private fun resultRowToReclamo(row: ResultRow) = Reclamo(
        idReclamo = row[Reclamos.idReclamo],
        descripcion = row[Reclamos.descripcion],
        estado = row[Reclamos.estado],
        documento = row[Reclamos.documento],
        idSitio = row[Reclamos.idSitio],
        idDesperfecto = row[Reclamos.idDesperfecto],
        idReclamoUnif = row[Reclamos.idReclamoUnif]
    )

    override suspend fun get10Reclamos(pagina: Int): List<Reclamo> = dbQuery {
        val offset = (pagina - 1) * 10
        println("todos")
        Reclamos.selectAll()
            .limit(10,offset.toLong())
            .map(::resultRowToReclamo)
    }

    override suspend fun get10ReclamosBySector(pagina: Int, sector: String): List<Reclamo> = dbQuery {
        val offset = (pagina - 1) * 10
        Reclamos.join(Reclamos, JoinType.INNER, additionalConstraint = { Reclamos.idDesperfecto eq Desperfectos.idDesperfecto })
            .join(Desperfectos, JoinType.INNER, additionalConstraint = { Desperfectos.idRubro eq Rubros.idRubro })
            .select { Rubros.descripcion like sector }
            .limit(10, offset.toLong())
            .map(::resultRowToReclamo)
    }

    override suspend fun get10ReclamosByDocumento(pagina: Int, documento: String): List<Reclamo> = dbQuery {
        val offset = (pagina - 1) * 10
        println("documento")
        Reclamos.select { Reclamos.documento like documento }
            .limit(10, offset.toLong())
            .map(::resultRowToReclamo)
    }

    override suspend fun getReclamoById(id: Int): Reclamo? = dbQuery {
        println("id")
        Reclamos.select { Reclamos.idReclamo eq id }
            .map(::resultRowToReclamo)
            .singleOrNull()
    }
}