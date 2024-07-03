package ar.edu.uade.utilities.containers

import ar.edu.uade.mappers.MapPublicacion
import ar.edu.uade.utilities.Autenticacion
import kotlinx.serialization.Serializable

@Serializable
data class AutenticacionPublicacion (
    val autenticacion: Autenticacion,
    val publicacion: MapPublicacion
)