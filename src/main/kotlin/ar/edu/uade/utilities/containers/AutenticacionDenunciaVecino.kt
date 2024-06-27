package ar.edu.uade.utilities.containers

import ar.edu.uade.utilities.Autenticacion
import kotlinx.serialization.Serializable

@Serializable
data class AutenticacionDenunciaVecino(
    val autenticacion: Autenticacion,
    val denuncia: ContainerDenunciaVecino

    )