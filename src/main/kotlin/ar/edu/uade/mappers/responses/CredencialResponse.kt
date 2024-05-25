package ar.edu.uade.mappers.responses

import kotlinx.serialization.Serializable

@Serializable
data class CredencialResponse(
    val documento: String,
    val email: String?,
    val primerIngreso: Boolean?
)