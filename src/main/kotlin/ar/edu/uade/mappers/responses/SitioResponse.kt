package ar.edu.uade.mappers.responses

import kotlinx.serialization.Serializable

@Serializable
data class SitioResponse(
    val idSitio: Int,
    val descripcion: String
)
