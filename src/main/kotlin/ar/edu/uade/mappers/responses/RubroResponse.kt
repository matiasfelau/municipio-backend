package ar.edu.uade.mappers.responses

import kotlinx.serialization.Serializable

@Serializable
data class RubroResponse(
    val idRubro: Int,
    val descripcion: String?
)
