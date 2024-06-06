package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Sitio
import ar.edu.uade.models.Sitio.*
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import ar.edu.uade.models.PermanenciaSitio.*
import ar.edu.uade.models.Reclamo
import ar.edu.uade.models.Reclamo.Reclamos
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class SitioDAOFacadeMySQL: SitioDAOFacade {

    private fun resultRowToSitio(row: ResultRow) = Sitio(
        idSitio = row[Sitios.idSitio],
        latitud = row[Sitios.latitud],
        longitud = row[Sitios.longitud],
        calle = row[Sitios.calle],
        numero = row[Sitios.numero],
        entreCalleA = row[Sitios.entreCalleA],
        entreCalleB = row[Sitios.entreCalleB],
        descripcion = row[Sitios.descripcion],
        aCargoDe = row[Sitios.aCargoDe],
        apertura = row[Sitios.apertura],
        cierre = row[Sitios.cierre],
        comentarios = row[Sitios.comentarios]
    )
    override suspend fun getAllSitios(): List<Sitio> = dbQuery{
        Sitios.join(
            PermanenciaSitios,JoinType.INNER, additionalConstraint = {PermanenciaSitios.idSitio eq Sitios.idSitio}
        ).select{
            PermanenciaSitios.permanente eq true
        }.map(::resultRowToSitio)
    }

    override suspend fun addNewSitio(sitio: Sitio): Sitio? = dbQuery {
        val insertStatement = Sitios.insert{
            it[Sitios.latitud] = sitio.latitud
            it[Sitios.longitud] = sitio.longitud
            it[Sitios.calle] = sitio.calle
            it[Sitios.numero] = sitio.numero
            it[Sitios.entreCalleA] = sitio.entreCalleA
            it[Sitios.entreCalleB] = sitio.entreCalleB
            it[Sitios.descripcion] = sitio.descripcion
            it[Sitios.aCargoDe] = sitio.aCargoDe
            it[Sitios.apertura] = sitio.apertura
            it[Sitios.cierre] = sitio.cierre
            it[Sitios.comentarios] = sitio.comentarios
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToSitio)
    }

    override suspend fun getSitioById(idSitio: Int): Sitio? = dbQuery {
        Sitios.select { Sitios.idSitio eq idSitio }.map(::resultRowToSitio).singleOrNull()
    }
}

