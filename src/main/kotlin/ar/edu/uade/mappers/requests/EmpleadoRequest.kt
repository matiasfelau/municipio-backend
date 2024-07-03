package ar.edu.uade.mappers.requests

import kotlinx.serialization.Serializable

@Serializable
data class EmpleadoRequest(val legajo: Int, val password: String)