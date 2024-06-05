package ar.edu.uade.utilities.containers

import ar.edu.uade.mappers.requests.SitioRequest
import ar.edu.uade.utilities.Autenticacion
import kotlinx.serialization.Serializable

@Serializable
data class AutenticacionSitio(
    val autenticacion: Autenticacion,
    val sitio: SitioRequest
)
