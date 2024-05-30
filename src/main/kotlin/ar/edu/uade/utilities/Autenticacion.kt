package ar.edu.uade.utilities

import kotlinx.serialization.Serializable

@Serializable
data class Autenticacion(
    val tipo: String,
    val token: String
)
