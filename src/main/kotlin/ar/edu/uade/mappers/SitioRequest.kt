package ar.edu.uade.mappers

import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class SitioRequest(val nombre: String, val latitud: Float, val longitud: Float, val calle: String?, val numero: Int, val entreCalleA: String?,
                        val entreCalleB: String?, val descripcion: String, val aCargoDe: String, val apertura: LocalTime, val cierre: LocalTime,
                        val comentarios: String)
