package ar.edu.uade.utilities.containers

import ar.edu.uade.mappers.MapComercio
import ar.edu.uade.utilities.Autenticacion
import kotlinx.serialization.Serializable

@Serializable
data class AutenticacionComercio (
    val autenticacion: Autenticacion,
    val comercio: MapComercio
)

