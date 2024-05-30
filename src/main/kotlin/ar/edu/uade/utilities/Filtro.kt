package ar.edu.uade.utilities

import kotlinx.serialization.Serializable

@Serializable
data class Filtro(
    val tipo: String,
    val dato: String
)