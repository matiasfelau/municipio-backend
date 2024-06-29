package ar.edu.uade.mappers.responses

import kotlinx.serialization.Serializable

@Serializable
data class DenunciadoResponse(
    var denunciado: String?
)