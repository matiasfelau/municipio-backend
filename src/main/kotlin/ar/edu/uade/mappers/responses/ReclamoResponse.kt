package ar.edu.uade.mappers.responses

import kotlinx.serialization.Serializable

@Serializable
data class ReclamoResponse(
    val idReclamo: Int?,
    val descripcion: String?,
    val estado: String?,
    val documento: String?,
    val idSitio: Int?
)
