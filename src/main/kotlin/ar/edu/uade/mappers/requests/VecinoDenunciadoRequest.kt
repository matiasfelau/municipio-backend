package ar.edu.uade.mappers.requests

import kotlinx.serialization.Serializable

@Serializable
data class VecinoDenunciadoRequest (
    val direccion: String,
    val nombre: String,
    val apellido: String
)