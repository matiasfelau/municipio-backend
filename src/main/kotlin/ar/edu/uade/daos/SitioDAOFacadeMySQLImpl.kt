package ar.edu.uade.daos

import ar.edu.uade.models.Empleado
import ar.edu.uade.models.Sitio.*
import ar.edu.uade.models.Sitio
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class SitioDAOFacadeMySQLImpl : SitioDAOFacade {

    private fun resultRowToSitio(row: ResultRow) = Sitio(
        idSitio = row[Sitios.idSitio],
        nombre = row[Sitios.nombre],
        latitud = row[Sitios.latitud].toFloat(),
        longitud = row[Sitios.longitud].toFloat(),
        calle = row[Sitios.calle],
        numero = row[Sitios.numero],
        entreCalleA = row[Sitios.entreCalleA],
        entreCalleB = row[Sitios.entreCalleB],
        descripcion = row[Sitios.descripcion],
        aCargoDe = row[Sitios.aCargoDe],
        apertura = row[Sitios.apertura],
        cierre = row[Sitios.cierre],
        comentarios = row[Sitios.comentarios],
    )

    override suspend fun addNewSitio(
        nombre: String,
        latitud: Float,
        longitud: Float,
        calle: String?,
        numero: Int,
        entreCalleA: String?,
        entreCalleB: String?,
        descripcion: String,
        aCargoDe: String,
        apertura: LocalTime,
        cierre: LocalTime,
        comentarios: String
    ): Sitio? {
        TODO("Not yet implemented")
    }

    override suspend fun allSitio(): List<Sitio> {
        TODO("Not yet implemented")
    }

    override suspend fun editSitio(
        idSitio: Int,
        nombre: String,
        latitud: Float,
        longitud: Float,
        calle: String?,
        numero: Int,
        entreCalleA: String?,
        entreCalleB: String?,
        descripcion: String,
        aCargoDe: String,
        apertura: LocalTime,
        cierre: LocalTime,
        comentarios: String
    ): Boolean? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSitio(idSitio: Int): Boolean? {
        TODO("Not yet implemented")
    }
}