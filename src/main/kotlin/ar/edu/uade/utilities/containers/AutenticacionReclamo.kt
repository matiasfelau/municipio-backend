package ar.edu.uade.utilities.containers

import ar.edu.uade.mappers.requests.ReclamoRequest
import ar.edu.uade.models.Reclamo
import ar.edu.uade.utilities.Autenticacion
import kotlinx.serialization.Serializable

@Serializable
data class AutenticacionReclamo(
    val autenticacion: Autenticacion,
    val reclamo: ReclamoRequest
)
