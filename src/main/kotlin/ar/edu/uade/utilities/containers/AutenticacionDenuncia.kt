package ar.edu.uade.utilities.containers

import ar.edu.uade.utilities.Autenticacion
import kotlinx.serialization.Serializable

@Serializable
data class AutenticacionDenuncia (
    val autenticacion: Autenticacion,
    val documento: String
)