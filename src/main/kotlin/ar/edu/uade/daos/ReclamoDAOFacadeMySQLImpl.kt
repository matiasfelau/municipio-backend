package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Desperfecto.Desperfectos
import ar.edu.uade.models.Reclamo
import ar.edu.uade.models.Reclamo.Reclamos
import ar.edu.uade.models.ReclamoImagen
import ar.edu.uade.models.Rubro.Rubros
import ar.edu.uade.utilities.Filtro
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like

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

    private fun resultRowToReclamoImagen(row: ResultRow) = ReclamoImagen(
        idReclamo = row[ReclamoImagen.ReclamoImagenes.idReclamo],
        urlImagen = row[ReclamoImagen.ReclamoImagenes.urlImagen]
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
        Reclamos.join(Desperfectos, JoinType.INNER, additionalConstraint = { Reclamos.idDesperfecto eq Desperfectos.idDesperfecto })
            .join(Rubros, JoinType.INNER, additionalConstraint = { Desperfectos.idRubro eq Rubros.idRubro })
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

    override suspend fun addReclamo(reclamo: Reclamo) = dbQuery{
        val insertStatement = Reclamos.insert{
            it[Reclamos.descripcion] = reclamo.descripcion
            it[Reclamos.estado] = reclamo.estado
            it[Reclamos.documento] = reclamo.documento
            it[Reclamos.idSitio] = reclamo.idSitio
            it[Reclamos.idDesperfecto] = reclamo.idDesperfecto
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToReclamo)
    }

    override suspend fun addImagenToReclamo(idReclamo: Int,urlImagen: String) = dbQuery{
        val insertStatement = ReclamoImagen.ReclamoImagenes.insert {
            it[ReclamoImagen.ReclamoImagenes.idReclamo] = idReclamo
            it[ReclamoImagen.ReclamoImagenes.urlImagen] = urlImagen
        }
    }

    override suspend fun getAllCantidadPaginas(): Int = dbQuery {
        Reclamos.selectAll()
            .map(::resultRowToReclamo)
            .count()
    }

    override suspend fun getAllCantidadPaginasByDocumento(documento: String): Int = dbQuery {
        Reclamos.select { Reclamos.documento like documento }
            .map(::resultRowToReclamo)
            .count()
    }

    override suspend fun getAllCantidadPaginasBySector(sector: String): Int = dbQuery {
        Reclamos.join(Desperfectos, JoinType.INNER, additionalConstraint = { Reclamos.idDesperfecto eq Desperfectos.idDesperfecto })
            .join(Rubros, JoinType.INNER, additionalConstraint = { Desperfectos.idRubro eq Rubros.idRubro })
            .select { Rubros.descripcion like sector }
            .map(::resultRowToReclamo)
            .count()
    }

    override suspend fun getFotosById(id: Int): List<ReclamoImagen> = dbQuery {
        ReclamoImagen.ReclamoImagenes.select{ ReclamoImagen.ReclamoImagenes.idReclamo eq id }
            .map(::resultRowToReclamoImagen)
    }
}