package ar.edu.uade.mappers.requests

import kotlinx.serialization.Serializable

@Serializable
data class DenunciaRequest (
    val descripcion: String?,
    val aceptarResponsabilidad: Boolean,
    val documento: String
)