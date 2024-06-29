package ar.edu.uade.mappers.responses

import kotlinx.serialization.Serializable

@Serializable
data class VecinoDenunciadoResponse(
    val idDenuncia: Int,
    val documento: String?,
    val direccion: String,
    val nombre: String,
    val apellido: String
)