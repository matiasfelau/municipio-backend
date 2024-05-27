package ar.edu.uade.mappers.requests
import kotlinx.serialization.Serializable
@Serializable
data class CredencialRequest(
    val documento: String,
    val password: String?,
    val email: String
)