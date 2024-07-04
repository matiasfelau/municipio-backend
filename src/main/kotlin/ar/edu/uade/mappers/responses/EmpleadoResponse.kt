package ar.edu.uade.mappers.responses

import kotlinx.serialization.Serializable

@Serializable
data class EmpleadoResponse(
    val legajo: Int,
    val nombre: String,
    val apellido: String,
    val documento: String?,
    val password: String,
    val sector: String
)
