package ar.edu.uade.mappers

import kotlinx.serialization.Serializable

@Serializable
data class EmpleadoRequest(val legajo: Int, val password: String)
