package ar.edu.uade.daos

import ar.edu.uade.models.Empleado
import ar.edu.uade.models.Sitio
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

interface SitioDAOFacade {
    suspend fun addNewSitio(
        nombre: String, //TODO AGREGADO POR NOSOTROS, VER RESPUESTA DE PROFESOR
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
    ): Sitio?
    suspend fun allSitio(): List<Sitio>
    suspend fun editSitio(
        idSitio: Int,
        nombre: String, //TODO AGREGADO POR NOSOTROS, VER RESPUESTA DE PROFESOR
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
    ): Boolean?
    suspend fun deleteSitio(idSitio: Int): Boolean?
}