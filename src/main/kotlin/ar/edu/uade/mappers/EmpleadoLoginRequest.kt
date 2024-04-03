package ar.edu.uade.mappers

import kotlinx.serialization.Serializable

@Serializable
data class EmpleadoLoginRequest(val legajo: Int, val password: String)
