package ar.edu.uade.mappers.requests

import kotlinx.serialization.Serializable

@Serializable
data class DenunciaRequest (
    val descripcion: String?,
    var estado: String?,
    val aceptarResponsabilidad: Boolean,
    val documento: String,
    val fileStrings: List<String>?
)