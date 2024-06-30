package ar.edu.uade.utilities.containers

import ar.edu.uade.mappers.MapProfesional
import ar.edu.uade.utilities.Autenticacion
import kotlinx.serialization.Serializable

@Serializable
data class AutenticacionProfesional(
    val autenticacion: Autenticacion,
    val profesional: MapProfesional
)
