package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Desperfecto
import ar.edu.uade.models.Sitio
import ar.edu.uade.models.Sitio.*
import ar.edu.uade.utilities.LocalDateTimeComponentSerializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import java.time.LocalDateTime

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
        Sitios.selectAll().map(::resultRowToSitio)
    }
}