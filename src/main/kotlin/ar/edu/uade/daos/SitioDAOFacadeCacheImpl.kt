package ar.edu.uade.daos

import ar.edu.uade.models.Sitio
import java.time.LocalTime
import java.util.*

class SitioDAOFacadeCacheImpl : SitioDAOFacade {
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