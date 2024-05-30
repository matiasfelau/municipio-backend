package ar.edu.uade.utilities.containers

import ar.edu.uade.utilities.Autenticacion
import ar.edu.uade.utilities.Filtro
import kotlinx.serialization.Serializable

@Serializable
data class AutenticacionFiltro(
    val autenticacion: Autenticacion,
    val filtro: Filtro
)
