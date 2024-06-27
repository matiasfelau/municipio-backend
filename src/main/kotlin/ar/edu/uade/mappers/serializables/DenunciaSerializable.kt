package ar.edu.uade.mappers.serializables

import kotlinx.serialization.Serializable

@Serializable
data class DenunciaSerializable (
    val idDenuncia: Int,
    val descripcion: String?,
    val estado: String?,
    val aceptarResponsabilidad: Boolean,
    val documento: String
)