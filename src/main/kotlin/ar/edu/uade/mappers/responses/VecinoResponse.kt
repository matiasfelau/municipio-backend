package ar.edu.uade.mappers.responses

import kotlinx.serialization.Serializable

@Serializable
data class VecinoResponse(
    val nombre: String,
    val apellido: String,
    val documento: String,
    val email: String,
    val password: String
) {

}