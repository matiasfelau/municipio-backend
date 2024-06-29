package ar.edu.uade.mappers.responses

import kotlinx.serialization.Serializable

@Serializable
data class ComercioDenunciadoResponse(
    val idComercio: Int,
    val idDenuncia: Int,
    val nombre: String,
    val direccion: String
)