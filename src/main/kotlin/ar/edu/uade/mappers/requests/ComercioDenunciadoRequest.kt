package ar.edu.uade.mappers.requests

import kotlinx.serialization.Serializable

@Serializable
data class ComercioDenunciadoRequest(
    val idComercio: Int,
    val nombre: String,
    val direccion: String
)