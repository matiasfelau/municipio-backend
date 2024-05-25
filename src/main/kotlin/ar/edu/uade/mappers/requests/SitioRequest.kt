package ar.edu.uade.mappers.requests

import ar.edu.uade.mappers.LocalTimeComponentSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class SitioRequest(val nombre: String,
                        val latitud: Float,
                        val longitud: Float,
                        val calle: String?,
                        val numero: Int,
                        val entreCalleA: String?,
                        val entreCalleB: String?,
                        val descripcion: String,
                        val aCargoDe: String,
                        @kotlinx.serialization.Serializable(with = LocalTimeComponentSerializer::class)
                        val apertura: LocalTime,
                        @kotlinx.serialization.Serializable(with = LocalTimeComponentSerializer::class)
                        val cierre: LocalTime,
                        val comentarios: String)
